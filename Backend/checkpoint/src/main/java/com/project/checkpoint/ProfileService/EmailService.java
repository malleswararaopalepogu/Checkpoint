package com.project.checkpoint.ProfileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailsender;
	
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	@Async
	public void sendWelcomeEmail(String toEmail,String name)
	{
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmail);
			message.setSubject("Welcome to Our Platform");
			message.setText("Hello "+name+",\n\nThanks for registering with us!\n\n Regards,\n Checkpoint Team");
			mailsender.send(message);
			System.out.println("Welcome email successfully sent to: " + toEmail);
		} catch (Exception e) {
			System.err.println("Failed to send welcome email to " + toEmail + ": " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Async
	public void sendResetOTPemail(String toEmail,String otp)
	{
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmail);
			message.setSubject("Password Reset OTP");
			message.setText("OTP for password Resetting is :"+otp);
			mailsender.send(message);
			System.out.println("Reset OTP email successfully sent to: " + toEmail);
		} catch (Exception e) {
			System.err.println("Failed to send reset OTP email to " + toEmail + ": " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Async
	public void sendOTP(String toEmail,String otp)
	{
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmail);
			message.setSubject("Account Verification OTP");
			message.setText("OTP for Account Verification is : "+otp);
			mailsender.send(message);
			System.out.println("Verification OTP email successfully sent to: " + toEmail);
		} catch (Exception e) {
			System.err.println("Failed to send verification OTP email to " + toEmail + ": " + e.getMessage());
			e.printStackTrace();
		}
	}
}

