package com.klu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.klu.entity.Notification;
import com.klu.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Long userId, String message, Long foodId) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setMessage(message);
        n.setFoodId(foodId);
        n.setIsRead(false);
        return notificationRepository.save(n);
    }

    public List<Notification> getByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}