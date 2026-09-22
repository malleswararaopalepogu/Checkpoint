package com.project.checkpoint.controller;

import org.springframework.http.HttpHeaders;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.checkpoint.ProfileService.AppUserDetailsService;
import com.project.checkpoint.io.AuthRequest;
import com.project.checkpoint.io.AuthResponse;
import com.project.checkpoint.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final AppUserDetailsService appUserDetailsService;
	private final JwtUtil jwtutil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request)
	{
		try {
			authenticate(request.getEmail(),request.getPassword());
			final UserDetails userDetails=appUserDetailsService.loadUserByUsername(request.getEmail());
			final String jwttoken=jwtutil.generateToken(userDetails);
			ResponseCookie cookie=ResponseCookie.from("jwt", jwttoken)
										.httpOnly(true)
										.path("/")
										.maxAge(Duration.ofDays(1))
										.sameSite("Strict")
										.build();
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
					.body(new AuthResponse(request.getEmail(),jwttoken));
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
			ex.printStackTrace();
			Map<String,Object> error=new HashMap<>();
			error.put("error", true);
			error.put("message","Authentication Failed");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
	
	private void authenticate(String email,String password)
	{
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(email,password);
		authenticationManager.authenticate(token);
	}
	
	
	
	
	
}
