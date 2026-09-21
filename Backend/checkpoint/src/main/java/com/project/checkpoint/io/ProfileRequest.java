package com.project.checkpoint.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

	@NotBlank(message = "Name should not be Empty")
	private String name;
	
	@Email(message = "Enter Valid Email address")
	@NotNull(message = "Email should not be Empty")
	private String email;
	
	@Size(min=6 , message = "Password must be 6 characters")
	private String password;
}

