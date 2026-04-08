package com.klu.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "food")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Column(nullable = false)
    private Double quantity;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String status = "AVAILABLE"; // AVAILABLE / CLAIMED / EXPIRED / WAREHOUSE

    @NotNull(message = "Donor id is required")
    @Column(nullable = false)
    private Long donorId;

    // Pickup map location
    @NotNull(message = "Latitude is required")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // claim window end (createdAt + 30 min)
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public Food() {
    }

    @PrePersist
    public void prePersist() {
        if (status == null || status.isBlank()) {
            status = "AVAILABLE";
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (expiresAt == null) {
            expiresAt = createdAt.plusMinutes(30);
        }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getQuantity() { return quantity; }
    public String getLocation() { return location; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }
    public Long getDonorId() { return donorId; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public void setLocation(String location) { this.location = location; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setStatus(String status) { this.status = status; }
    public void setDonorId(Long donorId) { this.donorId = donorId; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}