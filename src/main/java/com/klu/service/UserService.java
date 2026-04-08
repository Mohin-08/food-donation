package com.klu.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klu.entity.User;
import com.klu.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Public registration allows only DONOR/NGO
	public User registerPublic(User user) {
	    user.setEmail(normalizeEmail(user.getEmail()));
	    String role = normalizeRole(user.getRole());
	    if (!"DONOR".equals(role) && !"NGO".equals(role)) {
	        throw new IllegalArgumentException("Public signup supports only DONOR or NGO role");
	    }
	    user.setRole(role);
	    user.setPassword(encodePassword(user.getPassword()));
	    return repo.save(user);
	}

	// Admin-created account with explicit ADMIN role
	public User registerAdmin(User user) {
	    user.setEmail(normalizeEmail(user.getEmail()));
	    user.setRole("ADMIN");
	    user.setPassword(encodePassword(user.getPassword()));
	    return repo.save(user);
	}

	// Get all users
	public List<User> getAll() {
	    return repo.findAll();
	}

	// Get by ID
	public User getById(Long id) {
	    return repo.findById(id).orElse(null);
	}

	public User getByEmail(String email) {
	    return repo.findByEmail(normalizeEmail(email)).orElse(null);
	}

	public User authenticate(String email, String rawPassword) {
	    User user = repo.findByEmail(normalizeEmail(email))
	        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

	    String storedPassword = user.getPassword();
	    if (storedPassword == null || storedPassword.isBlank()) {
	        throw new BadCredentialsException("Invalid email or password");
	    }

	    if (passwordEncoder.matches(rawPassword, storedPassword)) {
	        return user;
	    }

	    if (storedPassword.equals(rawPassword)) {
	        user.setPassword(encodePassword(rawPassword));
	        return repo.save(user);
	    }

	    throw new BadCredentialsException("Invalid email or password");
	}

	// Update user
	public User update(Long id, User userData, boolean allowRoleChange) {
	    User existing = repo.findById(id).orElse(null);
	    if (existing == null) {
	        return null;
	    }

	    existing.setName(userData.getName());
	    existing.setEmail(normalizeEmail(userData.getEmail()));
	    if (userData.getPassword() != null && !userData.getPassword().isBlank()) {
	        existing.setPassword(encodePassword(userData.getPassword()));
	    }
	    if (allowRoleChange) {
	        existing.setRole(normalizeRole(userData.getRole()));
	    }
	    existing.setPhone(userData.getPhone());
	    existing.setNgoDescription(userData.getNgoDescription());
	    existing.setAddress(userData.getAddress());
	    existing.setLatitude(userData.getLatitude());
	    existing.setLongitude(userData.getLongitude());

	    return repo.save(existing);
	}

	// Delete user
	public void delete(Long id) {
	    repo.deleteById(id);
	}

	private String normalizeEmail(String email) {
	    return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
	}

	private String normalizeRole(String role) {
	    return role == null ? null : role.trim().toUpperCase(Locale.ROOT);
	}

	private String encodePassword(String password) {
	    if (password == null || password.isBlank()) {
	        return password;
	    }
	    if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
	        return password;
	    }
	    return passwordEncoder.encode(password);
	}
}