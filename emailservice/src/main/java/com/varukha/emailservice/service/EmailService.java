package com.varukha.emailservice.service;

import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;

/**
 * The EmailService interface defines methods for handling email messages.
 */
public interface EmailService {

    /**
     * Handles the email message represented by the provided EmailMessageDto object.
     *
     * @param emailMessageDto The email message to be handled.
     */
    void handleEmailMessage(EmailMessageDto emailMessageDto);
}
