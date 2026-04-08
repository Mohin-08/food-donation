package com.klu.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.Request;
import com.klu.service.RequestService;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "http://localhost:5173")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyRole('ADMIN', 'NGO')")
    public ResponseEntity<Request> requestFood(@Valid @RequestBody Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.requestFood(request));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Request>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/ngo/{id}", produces = "application/json")
    public ResponseEntity<List<Request>> getByNgo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByNgo(id));
    }

    @PutMapping(value = "/approve/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approveRequest(id));
    }
}