package com.varukha.emailservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;
import com.varukha.emailservice.exception.EmailListenerException;
import com.varukha.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service class for listening to email messages from Kafka.
 */
@Service
@RequiredArgsConstructor
public class EmailListener {
    private static final Logger LOGGER = LogManager.getLogger(EmailListener.class);

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    /**
     * Listens to the Kafka topic for email messages and processes them.
     *
     * @param emailJsonMessage The JSON message containing email data.
     */
    @KafkaListener(topics = "${kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String emailJsonMessage) {
        try {
            EmailMessageDto cargoMessage = objectMapper
                    .readValue(emailJsonMessage, EmailMessageDto.class);
            emailService.handleEmailMessage(cargoMessage);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing email JSON message: {}", e.getMessage(), e);
            throw new EmailListenerException("Error processing email JSON message", e);
        }
    }
}
