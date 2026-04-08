package com.klu.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.Notification;
import com.klu.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@userAccessService.canAccessUser(authentication, #userId)")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        return notificationService.getByUser(userId);
    }
}