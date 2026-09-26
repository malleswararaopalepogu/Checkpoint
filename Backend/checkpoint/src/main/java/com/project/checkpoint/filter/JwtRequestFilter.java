package com.project.checkpoint.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.checkpoint.ProfileService.AppUserDetailsService;
import com.project.checkpoint.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final AppUserDetailsService appUserDetailsService;
	private final JwtUtil jwtutil;
	
	private static final List<String> PUBLIC_URLS=List.of("/api/v1.0/login","/api/v1.0/register","/api/v1.0/send-reset-otp","/api/v1.0/reset-password","/api/v1.0/logout");
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath != null && !contextPath.isBlank() && requestUri.startsWith(contextPath)) {
			requestUri = requestUri.substring(contextPath.length());
		}
		if(PUBLIC_URLS.contains(requestUri))
		{
			filterChain.doFilter(request, response);
			return;
		}
		String jwt=null;
		String email=null;
		
		final String authorizationHeader=request.getHeader("Authorization");
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
		{
			jwt=authorizationHeader.substring(7);	
		}
		
		if(jwt==null)
		{
			Cookie[] cookies=request.getCookies();	
			if(cookies!=null)
			{
				for(Cookie cookie:cookies)
				{
					if("jwt".equals(cookie.getName()))
					{
						jwt=cookie.getValue();
						break;
					}
				}
			}
		}
		if(jwt!=null)
		{
			email=jwtutil.extractEmail(jwt);
			if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UserDetails userDetails=appUserDetailsService.loadUserByUsername(email);
				if(jwtutil.ValidateToken(jwt, userDetails))
				{
					UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		 
		filterChain.doFilter(request, response);
	}
	

}
