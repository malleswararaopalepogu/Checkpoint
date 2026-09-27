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
import org.springframework.http.HttpMethod;

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
		http
			.cors(cors -> cors.configurationSource(corsconfigurationsource()))
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth->auth
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.requestMatchers("/login", "/register", "/send-reset-otp", "/logout", "/reset-password", "/is-authenticated", "/error")
					.permitAll().anyRequest().authenticated())
			.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
	public CorsConfigurationSource corsconfigurationsource()
	{
		CorsConfiguration config=new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of(
			"*",
			"http://localhost:5173",
			"http://127.0.0.1:5173",
			"https://*.vercel.app"
		));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
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










