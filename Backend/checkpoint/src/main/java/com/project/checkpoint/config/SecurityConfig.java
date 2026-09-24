package com.project.checkpoint.config;

import java.util.List;

import org.springframework.security.authentication.ProviderManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.project.checkpoint.ProfileService.AppUserDetailsService;
import com.project.checkpoint.filter.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AppUserDetailsService appUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final customAuthenticationEntryPoint customauthenticationEntryPoint;
	
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception
	{
		http.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth->auth
					.requestMatchers("/login","/register","/send-reset-otp","/logout","/reset-password")
					.permitAll().anyRequest().authenticated())
			.sessionManagement(session->session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.logout(AbstractHttpConfigurer :: disable)
			.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(ex->ex.authenticationEntryPoint(customauthenticationEntryPoint));
		return http.build();
			
	}
	
	@Bean
	public PasswordEncoder passwordencoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CorsFilter corsfilter()
	{
		return new CorsFilter(corsconfigurationsource());
	}
	
	private CorsConfigurationSource corsconfigurationsource()
	{
		CorsConfiguration config=new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization","Content-Type"));
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public AuthenticationManager authenticationManager()
	{
		 DaoAuthenticationProvider authenticationprovider=new DaoAuthenticationProvider(appUserDetailsService);
		 authenticationprovider.setPasswordEncoder(passwordencoder());
		 
		 return new ProviderManager(authenticationprovider);
		
	}
}










