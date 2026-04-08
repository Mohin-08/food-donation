package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.dto.AnalyticsResponse;
import com.klu.repository.DonationRepository;
import com.klu.repository.FoodRepository;
import com.klu.repository.RequestRepository;

@Service
public class AnalyticsService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private DonationRepository donationRepository;

    public AnalyticsResponse getAnalytics() {
        return new AnalyticsResponse(
            foodRepository.count(),
            foodRepository.countByStatus("AVAILABLE"),
            foodRepository.countByStatus("CLAIMED"),
            requestRepository.count(),
            requestRepository.countByStatus("PENDING"),
            requestRepository.countByStatus("APPROVED"),
            requestRepository.countByStatus("REJECTED"),
            donationRepository.count()
        );
    }
}