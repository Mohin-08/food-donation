package com.klu.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.klu.entity.User;
import com.klu.repository.UserRepository;
import com.klu.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.substring(7);
		try {
			String email = jwtService.extractUsername(token);
			if (!StringUtils.hasText(email)) {
				writeUnauthorized(response, "Invalid token");
				return;
			}

			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				User user = userRepository.findByEmail(email.toLowerCase(Locale.ROOT)).orElse(null);
				if (user == null) {
					writeUnauthorized(response, "User not found for token");
					return;
				}

				if (!jwtService.isTokenValid(token, user.getEmail())) {
					writeUnauthorized(response, "Token expired or invalid");
					return;
				}

				List<SimpleGrantedAuthority> authorities = List.of(
						new SimpleGrantedAuthority("ROLE_" + normalizeRole(user.getRole()))
				);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user.getEmail(),
						null,
						authorities
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);
		} catch (RuntimeException ex) {
			writeUnauthorized(response, "Invalid or expired token");
		}
	}

	private String normalizeRole(String role) {
		return role == null ? "USER" : role.trim().toUpperCase(Locale.ROOT);
	}

	private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", 401);
		body.put("error", "Unauthorized");
		body.put("message", message);
		response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"" + escapeJson(message) + "\"}");
	}

	private String escapeJson(String value) {
		if (value == null) {
			return "Unauthorized";
		}
		return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", " ");
	}
}