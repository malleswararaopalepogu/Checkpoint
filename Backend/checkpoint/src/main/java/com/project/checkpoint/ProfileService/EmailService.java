package com.project.checkpoint.ProfileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailsender;
	
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	public void sendWelcomeEmail(String toEmail,String name)
	{
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Welcome to Our Platform");
		message.setText("Hello "+name+",\n\nThanks for registering with us!\n\n Regards,\n Checkpoint Team");
		mailsender.send(message);
	}
	
	public void sendResetOTPemail(String toEmail,String otp)
	{
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Password Reset OTP");
		message.setText("OTP for password Resetting is :"+otp);
		mailsender.send(message);
	}
	
	public void sendOTP(String toEmail,String otp)
	{
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Account Verification OTP");
		message.setText("OTP for Account Verification is : "+otp);
		mailsender.send(message);
	}
}
