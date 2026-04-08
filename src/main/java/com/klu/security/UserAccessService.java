package com.klu.security;

import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.klu.entity.User;
import com.klu.repository.UserRepository;

@Service
public class UserAccessService {
	private final UserRepository userRepository;

	public UserAccessService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean canAccessUser(Authentication authentication, Long userId) {
		if (authentication == null || !authentication.isAuthenticated() || userId == null) {
			return false;
		}

		if (isAdmin(authentication)) {
			return true;
		}

		return userRepository.findByEmail(authentication.getName().toLowerCase(Locale.ROOT))
				.map(user -> userId.equals(user.getId()))
				.orElse(false);
	}

	public boolean isAdmin(Authentication authentication) {
		if (authentication == null) {
			return false;
		}

		return authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_ADMIN".equalsIgnoreCase(authority.getAuthority()));
	}

	public boolean isRole(Authentication authentication, String role) {
		if (authentication == null || role == null) {
			return false;
		}

		String normalizedRole = role.trim().toUpperCase(Locale.ROOT);
		return authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_" + normalizedRole));
	}

	public User currentUser(Authentication authentication) {
		if (authentication == null || authentication.getName() == null) {
			return null;
		}
		return userRepository.findByEmail(authentication.getName().toLowerCase(Locale.ROOT)).orElse(null);
	}
}