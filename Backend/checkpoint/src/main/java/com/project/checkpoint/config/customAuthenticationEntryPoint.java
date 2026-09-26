package com.project.checkpoint.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class customAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		 System.out.println("AUTH_ENTRY_POINT => Method: " + request.getMethod()
			 + ", URI: " + request.getRequestURI()
			 + ", ContextPath: " + request.getContextPath()
			 + ", ServletPath: " + request.getServletPath()
			 + ", Exception: " + authException.getClass().getName() + ": " + authException.getMessage());
		 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		 response.setContentType("application/json");
		 response.getWriter().write("{\"authenticated\": false, \"message\": \"User is not authenticated\"}");
	}

}
