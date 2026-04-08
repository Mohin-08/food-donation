package com.klu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.klu.entity.AuditLog;
import com.klu.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private static final int MAX_LOG_COUNT = 500;

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLog save(AuditLog log) {
        AuditLog saved = auditLogRepository.save(log);
        pruneOldLogsIfNeeded();
        return saved;
    }

    private void pruneOldLogsIfNeeded() {
        long totalLogs = auditLogRepository.count();
        long overflow = totalLogs - MAX_LOG_COUNT;
        if (overflow <= 0) {
            return;
        }

        Pageable pageable = PageRequest.of(0, (int) overflow, Sort.by(Sort.Direction.ASC, "createdAt"));
        auditLogRepository.deleteAllInBatch(auditLogRepository.findAllByOrderByCreatedAtAsc(pageable));
    }

    public Page<AuditLog> getLogs(String q, String method, Integer status, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 200);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return auditLogRepository.search(q, method, status, pageable);
    }
}
