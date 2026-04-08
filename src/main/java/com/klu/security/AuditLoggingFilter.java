package com.klu.security;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.klu.entity.AuditLog;
import com.klu.entity.User;
import com.klu.repository.UserRepository;
import com.klu.service.AuditLogService;

@Component
public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public AuditLoggingFilter(AuditLogService auditLogService, UserRepository userRepository) {
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        if (!StringUtils.hasText(path)) {
            return true;
        }
        return path.startsWith("/admin/logs") || path.startsWith("/error") || path.startsWith("/favicon");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);

        String path = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();

        AuditLog log = new AuditLog();
        log.setMethod(method);
        log.setPath(path);
        log.setStatusCode(status);
        log.setAction(resolveAction(method, path));
        log.setEntityType(resolveEntityType(path));
        log.setEntityId(resolveEntityId(path));
        log.setIpAddress(resolveIp(request));
        log.setUserAgent(safeTrim(request.getHeader("User-Agent"), 500));
        log.setDetails(status >= 400 ? "FAILED" : "SUCCESS");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && StringUtils.hasText(authentication.getName())) {
            String email = authentication.getName().toLowerCase(Locale.ROOT);
            log.setActorEmail(email);
            User actor = userRepository.findByEmail(email).orElse(null);
            if (actor != null) {
                log.setActorUserId(actor.getId());
                log.setActorRole(actor.getRole());
            }
        }

        auditLogService.save(log);
    }

    private String resolveAction(String method, String path) {
        if ("POST".equalsIgnoreCase(method) && "/auth/login".equals(path)) return "AUTH_LOGIN";
        if ("POST".equalsIgnoreCase(method) && "/users".equals(path)) return "USER_REGISTER";
        if ("PUT".equalsIgnoreCase(method) && path.startsWith("/users/")) return "USER_UPDATE";
        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/users/")) return "USER_DELETE";
        if ("POST".equalsIgnoreCase(method) && "/foods".equals(path)) return "FOOD_CREATE";
        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/foods/")) return "FOOD_DELETE";
        if ("POST".equalsIgnoreCase(method) && "/requests".equals(path)) return "REQUEST_CREATE";
        if ("PUT".equalsIgnoreCase(method) && path.startsWith("/requests/approve/")) return "REQUEST_APPROVE";
        return method.toUpperCase(Locale.ROOT) + " " + path;
    }

    private String resolveEntityType(String path) {
        if (!StringUtils.hasText(path) || "/".equals(path)) return "SYSTEM";
        String[] parts = path.split("/");
        if (parts.length > 1 && StringUtils.hasText(parts[1])) {
            return parts[1].toUpperCase(Locale.ROOT);
        }
        return "SYSTEM";
    }

    private Long resolveEntityId(String path) {
        if (!StringUtils.hasText(path)) return null;
        String[] parts = path.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            String p = parts[i];
            if (p != null && p.matches("\\d+")) {
                try {
                    return Long.valueOf(p);
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        }
        return null;
    }

    private String resolveIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            String[] parts = forwarded.split(",");
            return safeTrim(parts[0], 100);
        }
        return safeTrim(request.getRemoteAddr(), 100);
    }

    private String safeTrim(String value, int maxLen) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.length() <= maxLen) return trimmed;
        return trimmed.substring(0, maxLen);
    }
}
