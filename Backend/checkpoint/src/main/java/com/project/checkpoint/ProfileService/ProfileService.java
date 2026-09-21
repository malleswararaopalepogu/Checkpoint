package com.project.checkpoint.ProfileService;

import com.project.checkpoint.io.ProfileRequest;
import com.project.checkpoint.io.ProfileResponse;

public interface ProfileService {

	ProfileResponse createProfile(ProfileRequest request);
}
