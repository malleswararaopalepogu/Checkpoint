package com.project.checkpoint.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;

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
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileResponse register(@Valid @RequestBody ProfileRequest request) 
	{
		ProfileResponse profileresponse=profileService.createProfile(request);
		emailservice.sendWelcomeEmail(profileresponse.getEmail(), profileresponse.getName());
		return profileresponse;
	}
	
	@GetMapping("/profile")
	public ProfileResponse getProfile(@CurrentSecurityContext(expression="authentication?.name") String email)
	{
		return profileService.getProfile(email);
	}
}





