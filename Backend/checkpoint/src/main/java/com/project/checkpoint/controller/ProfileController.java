package com.project.checkpoint.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.server.ResponseStatusException;

import com.project.checkpoint.ProfileService.EmailService;
import com.project.checkpoint.ProfileService.ProfileService;
import com.project.checkpoint.io.ProfileRequest;
import com.project.checkpoint.io.ProfileResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService  profileService;
	
	private final EmailService emailservice;
	
	@PostMapping("/register")
	public org.springframework.http.ResponseEntity<?> register(@Valid @RequestBody ProfileRequest request) 
	{
		try {
			ProfileResponse profileresponse = profileService.createProfile(request);
			try {
				emailservice.sendWelcomeEmail(profileresponse.getEmail(), profileresponse.getName());
			} catch (Exception e) {
				System.err.println("Failed to send welcome email: " + e.getMessage());
			}
			return org.springframework.http.ResponseEntity.status(HttpStatus.CREATED).body(profileresponse);
		} catch (ResponseStatusException ex) {
			java.util.Map<String, Object> error = new java.util.HashMap<>();
			error.put("error", true);
			error.put("message", ex.getReason() != null ? ex.getReason() : ex.getMessage());
			return org.springframework.http.ResponseEntity.status(ex.getStatusCode()).body(error);
		} catch (Exception ex) {
			java.util.Map<String, Object> error = new java.util.HashMap<>();
			error.put("error", true);
			error.put("message", "Registration failed: " + ex.getMessage());
			return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
	}
	
	@GetMapping("/profile")
	public ProfileResponse getProfile(@CurrentSecurityContext(expression="authentication?.name") String email)
	{
		return profileService.getProfile(email);
	}
}





