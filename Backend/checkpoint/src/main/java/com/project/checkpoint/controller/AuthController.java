package com.project.checkpoint.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.checkpoint.ProfileService.AppUserDetailsService;
import com.project.checkpoint.ProfileService.ProfileService;
import com.project.checkpoint.io.AuthRequest;
import com.project.checkpoint.io.AuthResponse;
import com.project.checkpoint.io.ResetPasswordRequest;
import com.project.checkpoint.util.JwtUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	@Value("${app.cookie.secure:false}")
	private boolean cookieSecure;

	@Value("${app.cookie.same-site:Lax}")
	private String cookieSameSite;

	private final AuthenticationManager authenticationManager;
	private final AppUserDetailsService appUserDetailsService;
	private final JwtUtil jwtutil;
	private final ProfileService profileService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request)
	{
		try {
			authenticate(request.getEmail(),request.getPassword());
			final UserDetails userDetails=appUserDetailsService.loadUserByUsername(request.getEmail());
			final String jwttoken=jwtutil.generateToken(userDetails);
			ResponseCookie cookie=ResponseCookie.from("jwt", jwttoken)
										.httpOnly(true)
									.secure(cookieSecure)
										.path("/")
										.maxAge(Duration.ofDays(1))
									.sameSite(cookieSameSite)
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
	
	@GetMapping("/is-authenticated")
	public ResponseEntity<Boolean> isAuthenticated(Authentication authentication)
	{
		boolean authenticated = authentication != null
				&& authentication.isAuthenticated()
				&& authentication.getPrincipal() instanceof UserDetails;
		return ResponseEntity.ok(authenticated);
	}
	
	@PostMapping("/send-reset-otp")
	public void sendResetOtp(@RequestParam String email)
	{
		try {
			profileService.sendResetOTP(email);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
	
	@PostMapping("/reset-password")
	public void resetPassword(@Valid @RequestBody ResetPasswordRequest request)
	{
		try {
			profileService.resetPassword(request.getEmail(), request.getNewPassword(), request.getOtp());
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
	
	@PostMapping("/send-otp")
	public void sendVerifyOTP(@CurrentSecurityContext(expression = "authentication?.name") String email)
	{
			try {
				profileService.sendOTP(email);
			} catch (Exception e) {
				 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			} 
	}
	
	@PostMapping("/verify-account")
	public void  verifyAccount(@RequestBody Map<String,Object> request, @CurrentSecurityContext(expression = "authentication?.name") String email) {
		 
		if(request.get("otp").toString()==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing the OTP");
		}
		try {
			profileService.verifyOTP(email,  request.get("otp").toString());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse reponse)
	{
		ResponseCookie cookie=ResponseCookie.from("jwt","")
										.httpOnly(true)
									.secure(cookieSecure)
										.path("/")
										.maxAge(0)
									.sameSite(cookieSameSite)
										.build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body("Logged Out Successfully");
	}
	
	
	
}
