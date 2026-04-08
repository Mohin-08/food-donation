package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.entity.Donation;
import com.klu.entity.Food;
import com.klu.entity.Request;
import com.klu.repository.DonationRepository;
import com.klu.repository.FoodRepository;
import com.klu.repository.RequestRepository;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepo;

    @Autowired
    private FoodRepository foodRepo;

    @Autowired
    private DonationRepository donationRepo;

    public Request requestFood(Request request) {
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            request.setStatus("PENDING");
        }
        return requestRepo.save(request);
    }

    public List<Request> getAll() {
        return requestRepo.findAllByOrderByCreatedAtDesc();
    }

    public List<Request> getByNgo(Long ngoId) {
        return requestRepo.findByNgoIdOrderByCreatedAtDesc(ngoId);
    }

    public String approveRequest(Long requestId) {
        Request req = requestRepo.findById(requestId).orElse(null);
        if (req == null) return "Request not found";

        if ("APPROVED".equals(req.getStatus())) {
            return "Request already approved";
        }

        req.setStatus("APPROVED");
        requestRepo.save(req);

        Food food = foodRepo.findById(req.getFoodId()).orElse(null);
        if (food != null) {
            food.setStatus("CLAIMED");
            foodRepo.save(food);

            Donation donation = new Donation();
            donation.setFoodId(food.getId());
            donation.setDonorId(food.getDonorId());
            donation.setNgoId(req.getNgoId());
            donationRepo.save(donation);
        }

        return "Request approved and donation recorded";
    }
}