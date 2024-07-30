package com.varukha.emailservice.service.impl;

import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;
import com.varukha.emailservice.mapper.EmailMessageMapper;
import com.varukha.emailservice.model.Email;
import com.varukha.emailservice.model.enums.EmailStatus;
import com.varukha.emailservice.repository.EmailRepository;
import com.varukha.emailservice.service.EmailService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for handling email messages.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final EmailMessageMapper emailMessageMapper;

    @Value("${spring.mail.username}")
    private String emailFrom;

    /**
     * Handles the incoming email message by sending it and updating its status.
     *
     * @param emailMessageDto The email message DTO to handle.
     */
    @Override
    public void handleEmailMessage(EmailMessageDto emailMessageDto) {
        Email email = emailMessageMapper.toModel(emailMessageDto);

        try {
            sendEmail(email.getTo(), email.getSubject(), email.getContent());
            email.setStatus(EmailStatus.SENT);
            LOGGER.info("Email sent successfully to {}", email.getTo());
        } catch (Exception e) {
            email.setStatus(EmailStatus.ERROR);
            email.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
            LOGGER.error("Failed to send email to {}: {}", email.getTo(), e.getMessage());
        } finally {
            email.setLastAttemptTime(Instant.now());
            emailRepository.save(email);
            LOGGER.info("Email status updated to {} and saved for {}",
                    email.getStatus(), email.getTo());
        }
    }

    /**
     * Sends an email with the given details.
     *
     * @param to          The recipient email address.
     * @param subject     The email subject.
     * @param messageBody The email message body.
     */
    private void sendEmail(String to, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageBody);
        mailSender.send(message);
    }

    /**
     * Scheduled task to retry sending failed emails periodically.
     * Retries every 5 minutes (300,000 milliseconds).
     */
    @Scheduled(fixedRate = 300000)
    private void retryFailedEmails() {
        List<Email> failedEmails = emailRepository.findByStatus(EmailStatus.ERROR);

        for (Email email : failedEmails) {
            try {
                sendEmail(email.getTo(), email.getSubject(), email.getContent());
                email.setStatus(EmailStatus.SENT);
                LOGGER.info("Email sent successfully after retry to {}", email.getTo());
            } catch (Exception e) {
                email.setStatus(EmailStatus.ERROR);
                email.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
                LOGGER.error("Failed to resend email after retry to {}: {}",
                        email.getTo(), e.getMessage());
            } finally {
                email.setAttemptCount(email.getAttemptCount() + 1);
                email.setLastAttemptTime(Instant.now());
                emailRepository.save(email);
                LOGGER.info("Email retry status updated and saved for {}", email.getTo());
            }
        }
    }
}
