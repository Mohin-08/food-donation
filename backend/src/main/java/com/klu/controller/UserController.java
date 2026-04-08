package com.klu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.User;
import com.klu.security.UserAccessService;
import com.klu.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private UserAccessService userAccessService;

	// Register User
	@PostMapping
	public User register(@RequestBody User user) {
	    return service.registerPublic(user);
	}

	// Admin creates another admin
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public User createAdmin(@RequestBody User user) {
	    return service.registerAdmin(user);
	}

	// Get All Users
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers() {
	    return service.getAll();
	}

	// Get User by ID
	@GetMapping("/{id}")
	@PreAuthorize("@userAccessService.canAccessUser(authentication, #id)")
	public User getById(@PathVariable Long id) {
	    return service.getById(id);
	}

	// Update User
	@PutMapping("/{id}")
	@PreAuthorize("@userAccessService.canAccessUser(authentication, #id)")
	public User updateUser(@PathVariable Long id, @RequestBody User user, Authentication authentication) {
	    boolean allowRoleChange = userAccessService.isAdmin(authentication);
	    return service.update(id, user, allowRoleChange);
	}

	// Delete User
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable Long id) {
	    service.delete(id);
	    return "User deleted successfully";
	}
}