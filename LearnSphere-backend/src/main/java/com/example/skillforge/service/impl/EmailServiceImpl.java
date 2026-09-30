package com.example.skillforge.service.impl;

import com.example.skillforge.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private boolean isMailConfigured() {
        return fromEmail != null && !fromEmail.isBlank() && !fromEmail.toLowerCase().contains("placeholder");
    }

    @Async
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        if (!isMailConfigured()) {
            log.warn("[EMAIL DIAGNOSTIC] SMTP credentials not configured. Skipping HTML email dispatch to recipient [{}].", to);
            return;
        }

        try {
            log.info("Sending email to recipient [{}]", to);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "LearnSphereSystem");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true = html

            emailSender.send(message);
            log.info("Email sent successfully to recipient [{}]", to);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send email to recipient [{}]: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendEnrollmentNotification(String studentEmail, String studentName, String courseTitle,
            String instructorName) {
        if (!isMailConfigured()) {
            log.warn("[EMAIL DIAGNOSTIC] SMTP credentials not configured. Skipping enrollment notification to recipient [{}].", studentEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(studentEmail);
            message.setSubject("Welcome to " + courseTitle + "!");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Congratulations! You have been successfully enrolled in the course:\n\n" +
                            "📚 Course: %s\n" +
                            "👨‍🏫 Instructor: %s\n\n" +
                            "You can now access the course materials and start learning.\n\n" +
                            "Log in to your LearnSphere account to get started:\n" +
                            "http://localhost:5173/dashboard\n\n" +
                            "Happy Learning!\n\n" +
                            "Best regards,\n" +
                            "LearnSphere Team",
                    studentName,
                    courseTitle,
                    instructorName);

            message.setText(emailBody);
            emailSender.send(message);

            log.info("Enrollment notification sent to recipient [{}]", studentEmail);
        } catch (Exception e) {
            log.error("Failed to send enrollment notification to recipient [{}]: {}", studentEmail, e.getMessage());
        }
    }

    @Override
    public void sendCourseAnnouncement(String studentEmail, String studentName, String courseTitle,
            String instructorName, String subject, String message) {
        if (!isMailConfigured()) {
            log.warn("[EMAIL DIAGNOSTIC] SMTP credentials not configured. Skipping announcement to recipient [{}].", studentEmail);
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(studentEmail);
            mailMessage.setSubject(subject);

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "%s\n\n" +
                            "---\n" +
                            "Course: %s\n" +
                            "Instructor: %s\n\n" +
                            "Best regards,\n" +
                            "LearnSphere Team",
                    studentName,
                    message,
                    courseTitle,
                    instructorName);

            mailMessage.setText(emailBody);
            emailSender.send(mailMessage);

            log.info("Course announcement sent to recipient [{}]", studentEmail);
        } catch (Exception e) {
            log.error("Failed to send announcement to recipient [{}]: {}", studentEmail, e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(String to, String name, String code) {
        if (!isMailConfigured()) {
            log.warn("[EMAIL DIAGNOSTIC] SMTP credentials not configured. Live email delivery to recipient [{}] skipped until SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD are set.", to);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Verify Your Account - LearnSphere-Platform");
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Thank you for registering with LearnSphere-Platform. Please use the verification code below to activate your account:\n\n" +
                "VERIFICATION CODE: %s\n\n" +
                "This code will expire in 15 minutes.\n\n" +
                "If you did not create an account, please ignore this email.\n\n" +
                "Best regards,\n" +
                "LearnSphere Team",
                name, code
            );
            
            message.setText(emailBody);
            emailSender.send(message);
            log.info("Verification email dispatched successfully via Gmail SMTP to recipient [{}]", to);
        } catch (Exception e) {
            log.error("Failed to dispatch verification email to recipient [{}]: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendContactAdminEmail(String instructorEmail, String instructorName, String subject, String messageContent) {
        if (!isMailConfigured()) {
            log.warn("[EMAIL DIAGNOSTIC] SMTP credentials not configured. Skipping instructor query email from [{}].", instructorEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("khariharan.personal@gmail.com"); 
            message.setSubject("Instructor Query: " + subject);
            
            String emailBody = String.format(
                "Dear Admin,\n\n" +
                "You have received a new message from an Instructor.\n\n" +
                "Instructor: %s (%s)\n\n" +
                "Subject: %s\n\n" +
                "Message:\n%s\n\n" +
                "---\n" +
                "LearnSphere System",
                instructorName, instructorEmail, subject, messageContent
            );
            
            message.setText(emailBody);
            emailSender.send(message);
            log.info("Contact Admin email sent from instructor [{}]", instructorEmail);
        } catch (Exception e) {
            log.error("Failed to send contact admin email from instructor [{}]: {}", instructorEmail, e.getMessage());
        }
    }
}
