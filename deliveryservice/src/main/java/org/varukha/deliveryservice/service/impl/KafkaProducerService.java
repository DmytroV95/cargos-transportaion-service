package org.varukha.deliveryservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

/**
 * The KafkaProducerService class is responsible for sending messages to a Kafka topic.
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    /**
     * The topic to which messages will be sent.
     */
    @Value("${kafka.topic.email}")
    private String emailTopic;

    /**
     * The KafkaOperations instance used for producing messages.
     */
    private final KafkaOperations<String, String> kafkaOperations;

    /**
     * Sends the specified message to the Kafka topic.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        kafkaOperations.send(emailTopic, message);
    }
}
