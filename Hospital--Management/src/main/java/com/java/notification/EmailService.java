package com.java.notification;


import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendAppointmentConfirmation(String toEmail, String patientName, String doctorName, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("Appointment Confirmation");
            helper.setText(buildEmailContent(patientName, doctorName, date, startTime, endTime), true);
            
            mailSender.send(message);
            logger.info("Appointment confirmation email sent successfully to {}", toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private String buildEmailContent(String patientName, String doctorName, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return "<h2>Appointment Confirmation</h2>" +
               "<p>Dear " + patientName + ",</p>" +
               "<p>Your appointment has been successfully booked with <b>Dr. " + doctorName + "</b>.</p>" +
               "<p><b>Date:</b> " + date + "<br>" +
               "<b>Time:</b> " + startTime + " - " + endTime + "</p>" +
               "<p>Thank you for choosing our hospital.</p>";
    }
}
