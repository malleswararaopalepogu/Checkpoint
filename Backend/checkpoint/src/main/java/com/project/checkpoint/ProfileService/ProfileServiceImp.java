package com.project.checkpoint.ProfileService;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;    

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	private final EmailService emailService;
	
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

	@Override
	public ProfileResponse getProfile(String email) {
		Userentity userentity= userRepo.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("User Not Found :"+email));
		return convertToProfileResponse(userentity);
	}

	@Override
	public void sendResetOTP(String email) {
		Userentity existingUser= userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email :"+email)); 
		
		//generate OTP
		String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		
		//expire time
		long expireTime=System.currentTimeMillis()+1000*60*15;	
		
		existingUser.setResetOtp(otp);
		existingUser.setResetOtpExpireAt(expireTime);
		
		userRepo.save(existingUser);
		
		try {
			emailService.sendResetOTPemail(email, otp);
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException	("Unable to send email");
		}
	}

	@Override
	public void resetPassword(String email,String newpassword, String otp) {
		Userentity existinguser=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("username not found with : "+email));
		if(existinguser.getResetOtp()==null && !existinguser.getResetOtp().equals(otp) )
		{
			throw new RuntimeException("Invalid OTP");
		}
		if(existinguser.getResetOtpExpireAt()<System.currentTimeMillis()) {
			throw new RuntimeException();
		}
		existinguser.setPassword(passwordencoder.encode(newpassword));
		existinguser.setResetOtp(null);
		existinguser.setResetOtpExpireAt(0L);
		
		userRepo.save(existinguser);
	}

	@Override
	public void sendOTP(String email) {
		Userentity existinguser=userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with :"+email));
		if(existinguser.getIsAccountVerified()!=null && existinguser.getIsAccountVerified())
		{
			return;
		}
		//generate OTP
		String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
				
		//expire time
		long expireTime=System.currentTimeMillis()+1000*60*60*24;
		existinguser.setVerifyOtp(otp);
		existinguser.setVerifyOtpExpireAt(expireTime);
		
		userRepo.save(existinguser);	
		
		try {
			emailService.sendOTP(email, otp);
		} catch (Exception e) {
			 throw new RuntimeException("Unable to send OTP");
		}
	}

	@Override
	public void verifyOTP(String email, String otp) {
		Userentity existinguser=userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with :"+email));
		if(existinguser.getVerifyOtp()==null || !existinguser.getVerifyOtp().equals(otp))
		{
			throw new RuntimeException("Invalid OTP");
		}
		
		if(existinguser.getVerifyOtpExpireAt()<System.currentTimeMillis())
		{
			throw new RuntimeException("OTP is Expired");
		}
		
		existinguser.setIsAccountVerified(true);
		existinguser.setVerifyOtp(null);
		existinguser.setVerifyOtpExpireAt(0L);
		userRepo.save(existinguser);
	}

}
