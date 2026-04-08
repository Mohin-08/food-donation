package com.klu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Long actorUserId;

    @Column(length = 255)
    private String actorEmail;

    @Column(length = 40)
    private String actorRole;

    @Column(nullable = false, length = 120)
    private String action;

    @Column(length = 80)
    private String entityType;

    private Long entityId;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 255)
    private String path;

    private Integer statusCode;

    @Column(length = 100)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 1000)
    private String details;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getActorUserId() { return actorUserId; }
    public String getActorEmail() { return actorEmail; }
    public String getActorRole() { return actorRole; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public Integer getStatusCode() { return statusCode; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getDetails() { return details; }

    public void setId(Long id) { this.id = id; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setActorUserId(Long actorUserId) { this.actorUserId = actorUserId; }
    public void setActorEmail(String actorEmail) { this.actorEmail = actorEmail; }
    public void setActorRole(String actorRole) { this.actorRole = actorRole; }
    public void setAction(String action) { this.action = action; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public void setMethod(String method) { this.method = method; }
    public void setPath(String path) { this.path = path; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public void setDetails(String details) { this.details = details; }
}
