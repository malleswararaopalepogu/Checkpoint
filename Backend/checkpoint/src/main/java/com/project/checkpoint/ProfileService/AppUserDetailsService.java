package com.project.checkpoint.ProfileService;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.checkpoint.entity.Userentity;
import com.project.checkpoint.repository.UserRepository;

import lombok.RequiredArgsConstructor;
 
@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Userentity existinguser=userRepo.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("Email not found for the email"+email));
		return new User(existinguser.getEmail(),existinguser.getPassword(),new ArrayList<>());
	}
}
