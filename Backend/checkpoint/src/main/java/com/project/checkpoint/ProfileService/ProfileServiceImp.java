package com.project.checkpoint.ProfileService;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.checkpoint.entity.Userentity;
import com.project.checkpoint.io.ProfileRequest;
import com.project.checkpoint.io.ProfileResponse;
import com.project.checkpoint.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService {
 
	private final UserRepository userRepo;
	private final PasswordEncoder passwordencoder;
	
	public ProfileResponse createProfile(ProfileRequest request) {
		Userentity newProfile = convertToUserEntity(request);
		if(!userRepo.existsByEmail(request.getEmail())) {
			newProfile = userRepo.save(newProfile);
			return  convertToProfileResponse(newProfile);
		}
		throw new ResponseStatusException(HttpStatus.CONFLICT,"Email Already exists");
	}
	
	private Userentity convertToUserEntity(ProfileRequest request)
	{
		return Userentity.builder()
				.userId(UUID.randomUUID().toString())
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordencoder.encode(request.getPassword()))
				.verifyOtp(null)
				.isAccountVerified(false)
				.verifyOtpExpireAt(0L)
				.resetOtp(null)
				.resetOtpExpireAt(0L)
				.build();
						
	}
	
	private ProfileResponse convertToProfileResponse(Userentity newProfile)
	{
		return ProfileResponse.builder()
				.UserId(newProfile.getUserId())
				.name(newProfile.getName())
				.email(newProfile.getEmail())
				.isAccountVerified(newProfile.getIsAccountVerified())
				.build();
	}

}
