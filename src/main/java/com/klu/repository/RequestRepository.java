package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByOrderByCreatedAtDesc();
    List<Request> findByNgoId(Long ngoId);
    List<Request> findByNgoIdOrderByCreatedAtDesc(Long ngoId);
    long countByStatus(String status);
}