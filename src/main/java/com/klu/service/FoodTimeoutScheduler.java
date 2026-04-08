package com.klu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.klu.entity.Food;
import com.klu.entity.User;
import com.klu.repository.FoodRepository;
import com.klu.repository.UserRepository;

@Component
public class FoodTimeoutScheduler {

    private final FoodRepository foodRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public FoodTimeoutScheduler(FoodRepository foodRepo, UserRepository userRepo, NotificationService notificationService) {
        this.foodRepo = foodRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    // Runs every minute
    @Scheduled(cron = "0 * * * * *")
    public void moveExpiredFoodToWarehouse() {
        List<Food> expiredAvailableFoods =
                foodRepo.findByStatusAndExpiresAtBefore("AVAILABLE", LocalDateTime.now());

        if (expiredAvailableFoods.isEmpty()) {
            return;
        }

        List<User> admins = userRepo.findByRole("ADMIN");

        for (Food food : expiredAvailableFoods) {
            food.setStatus("WAREHOUSE");
            foodRepo.save(food);

            for (User admin : admins) {
                String msg = "Unclaimed food moved to warehouse: " + food.getTitle()
                        + " (Food ID: " + food.getId() + ")";
                notificationService.createNotification(admin.getId(), msg, food.getId());
            }
        }
    }
}