package com.klu.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.Food;
import com.klu.service.FoodService;

@RestController
@RequestMapping("/foods")
@CrossOrigin(origins = "http://localhost:5173")
public class FoodController {

    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR')")
    public ResponseEntity<Food> addFood(@Valid @RequestBody Food food) {
        Food saved = service.addFood(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Food>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/available", produces = "application/json")
    public ResponseEntity<List<Food>> getAvailable() {
        return ResponseEntity.ok(service.getAvailableFood());
    }

    @GetMapping(value = "/donor/{id}", produces = "application/json")
    public ResponseEntity<List<Food>> getByDonor(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByDonor(id));
    }

    @GetMapping(value = "/warehouse", produces = "application/json")
    public ResponseEntity<List<Food>> getWarehouseFoods() {
        return ResponseEntity.ok(service.getWarehouseFoods());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Food deleted");
    }
}