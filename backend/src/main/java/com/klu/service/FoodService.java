package com.klu.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.klu.entity.Food;
import com.klu.entity.User;
import com.klu.repository.FoodRepository;
import com.klu.repository.UserRepository;
import com.klu.utill.DistanceUtill;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FoodService {

    private final FoodRepository foodRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public FoodService(FoodRepository foodRepo, UserRepository userRepo, NotificationService notificationService) {
        this.foodRepo = foodRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    public Food addFood(Food food) {
        LocalDateTime now = LocalDateTime.now();

        if (food.getStatus() == null || food.getStatus().isBlank()) {
            food.setStatus("AVAILABLE");
        }
        if (food.getCreatedAt() == null) {
            food.setCreatedAt(now);
        }
        if (food.getExpiresAt() == null) {
            food.setExpiresAt(now.plusMinutes(30));
        }

        Food savedFood = foodRepo.save(food);

        // Notify nearest 2 NGOs only
        if (savedFood.getLatitude() != null && savedFood.getLongitude() != null) {
            List<User> nearestNgos = userRepo.findByRole("NGO")
                    .stream()
                    .filter(u -> u.getLatitude() != null && u.getLongitude() != null)
                    .sorted(Comparator.comparingDouble(
                            ngo -> DistanceUtill.haversineKm(
                                    savedFood.getLatitude(),
                                    savedFood.getLongitude(),
                                    ngo.getLatitude(),
                                    ngo.getLongitude()
                            )))
                    .limit(2)
                    .collect(Collectors.toList());

            for (User ngo : nearestNgos) {
                String msg = "New nearby food available: " + savedFood.getTitle()
                        + " at " + savedFood.getLocation()
                        + ". Claim window: 30 minutes.";
                notificationService.createNotification(ngo.getId(), msg, savedFood.getId());
            }
        }

        return savedFood;
    }

    public List<Food> getAll() {
        return foodRepo.findAll();
    }

    public List<Food> getAvailableFood() {
        return foodRepo.findByStatus("AVAILABLE");
    }

    public List<Food> getByDonor(Long donorId) {
        return foodRepo.findByDonorId(donorId);
    }

    public List<Food> getWarehouseFoods() {
        return foodRepo.findByStatus("WAREHOUSE");
    }

    public void delete(Long id) {
        if (!foodRepo.existsById(id)) {
            throw new EntityNotFoundException("Food not found with id: " + id);
        }
        foodRepo.deleteById(id);
    }
}