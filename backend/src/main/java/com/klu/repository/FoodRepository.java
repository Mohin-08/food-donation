package com.klu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByStatus(String status);

    List<Food> findByDonorId(Long donorId);

    long countByStatus(String status);

    List<Food> findByStatusAndExpiresAtBefore(String status, LocalDateTime time);
}