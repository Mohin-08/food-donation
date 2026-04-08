package com.klu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name is required")
	@Column(nullable = false)
	private String name;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	@Column(nullable = false, unique = true)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = "Password is required")
	@Column(nullable = false)
	private String password;

	@NotBlank(message = "Role is required")
	@Column(nullable = false)
	private String role; // ADMIN / DONOR / NGO

	@NotBlank(message = "Phone is required")
	@Column(nullable = false)
	private String phone;

	@Column(name = "ngo_description")
	private String ngoDescription;

	@NotBlank(message = "Address is required")
	@Column(nullable = false)
	private String address;

	// Needed for NGO distance targeting
	private Double latitude;
	private Double longitude;

	public User() {
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public String getRole() { return role; }
	public String getPhone() { return phone; }
	public String getNgoDescription() { return ngoDescription; }
	public String getAddress() { return address; }
	public Double getLatitude() { return latitude; }
	public Double getLongitude() { return longitude; }

	public void setId(Long id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setEmail(String email) { this.email = email; }
	public void setPassword(String password) { this.password = password; }
	public void setRole(String role) { this.role = role; }
	public void setPhone(String phone) { this.phone = phone; }
	public void setNgoDescription(String ngoDescription) { this.ngoDescription = ngoDescription; }
	public void setAddress(String address) { this.address = address; }
	public void setLatitude(Double latitude) { this.latitude = latitude; }
	public void setLongitude(Double longitude) { this.longitude = longitude; 
	}
}
	