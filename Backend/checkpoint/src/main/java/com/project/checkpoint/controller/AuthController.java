package com.project.checkpoint.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.checkpoint.io.AuthRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;


	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request)
	{
		try {
			authenticate(request.getEmail(),request.getPassword());
		}catch(BadCredentialsException ex)
		{
			Map<String,Object> error=new HashMap<>();
			error.put("error", true);
			error.put("message","Email or password is incorrect");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
		catch(DisabledException ex) {
			Map<String,Object> error=new HashMap<>();
			error.put("error", true);
			error.put("message","Email or password is incorrect");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
		catch(Exception ex)
		{
			Map<String,Object> error=new HashMap<>();
			error.put("error", true);
			error.put("message","Authentication Failed");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
	
	private void authenticate(String email,String password)
	{
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}
	
	
	
	
	
}
