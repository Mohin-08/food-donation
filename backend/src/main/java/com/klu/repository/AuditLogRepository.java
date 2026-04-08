package com.klu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klu.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

  java.util.List<AuditLog> findAllByOrderByCreatedAtAsc(Pageable pageable);

    @Query(value = """
            SELECT a FROM AuditLog a
            WHERE (:q IS NULL OR :q = ''
                   OR LOWER(a.action) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(a.path) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(COALESCE(a.actorEmail, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(COALESCE(a.entityType, '')) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:method IS NULL OR :method = '' OR a.method = :method)
              AND (:status IS NULL OR a.statusCode = :status)
            ORDER BY a.createdAt DESC
            """,
            countQuery = """
            SELECT COUNT(a) FROM AuditLog a
            WHERE (:q IS NULL OR :q = ''
                   OR LOWER(a.action) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(a.path) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(COALESCE(a.actorEmail, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(COALESCE(a.entityType, '')) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:method IS NULL OR :method = '' OR a.method = :method)
              AND (:status IS NULL OR a.statusCode = :status)
            """)
    Page<AuditLog> search(@Param("q") String q,
                          @Param("method") String method,
                          @Param("status") Integer status,
                          Pageable pageable);
}
