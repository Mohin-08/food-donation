package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}