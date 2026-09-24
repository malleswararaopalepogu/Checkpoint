package com.project.checkpoint.ProfileService;

import com.project.checkpoint.io.ProfileRequest;
import com.project.checkpoint.io.ProfileResponse;

public interface ProfileService {

	ProfileResponse createProfile(ProfileRequest request);
	ProfileResponse getProfile(String email);
	void sendResetOTP(String email);
	void resetPassword(String email,String newpassword,String otp);
	void sendOTP(String email);
	void verifyOTP(String email,String otp);
}
