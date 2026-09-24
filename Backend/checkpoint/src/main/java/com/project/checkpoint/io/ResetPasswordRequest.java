package com.project.checkpoint.io;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

	@NotNull(message = "newpassword is required")
	private String newpassword;
	
	@NotNull(message= "otp is required")
	private String otp;
	
	@NotNull(message = "Email is required")
	private String email;
}
