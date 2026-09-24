package com.project.checkpoint.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${jwt.secrete.key}")
	private  String SECRETE_KEY;
	
	public String generateToken(UserDetails userDetails)
	{
		Map<String,Object> claims=new HashMap<>();
		return createToken(claims,userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
				.signWith(SignatureAlgorithm.HS256,SECRETE_KEY)
				.compact();
	}
	
	private Claims extractAllClaims(String token)
	{
		return Jwts.parser()
				.setSigningKey(SECRETE_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
	
	public <T> T extractClaims(String token,Function<Claims, T> ClaimResolver)
	{
		final Claims claims=extractAllClaims(token);
		return ClaimResolver.apply(claims);
	} 
	
	public String extractEmail(String token)
	{
		return extractClaims(token,Claims::getSubject);
	}
	
	public Date extractExperation(String token)
	{
		return extractClaims(token,Claims::getExpiration);
	}
	
	private Boolean isTokenExpired(String token)
	{
		return extractExperation(token).before(new Date());
	}
	
	public Boolean ValidateToken(String token,UserDetails userDetails)
	{
		final String email=extractEmail(token);
		return (!isTokenExpired(token) && email.equals(userDetails.getUsername()));
	}
	
	
	
	
	
	
}
