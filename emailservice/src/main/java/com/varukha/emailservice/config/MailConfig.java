package com.varukha.emailservice.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration class for Mail sender.
 */
@Configuration
public class MailConfig {
    @Value("${spring.mail.username}")
    private String apiMailUserName;

    @Value("${spring.mail.password}")
    private String apiMailPassword;

    /**
     * Configures and creates a JavaMailSender instance for sending emails.
     *
     * @return The configured JavaMailSender instance.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(apiMailUserName);
        mailSender.setPassword(apiMailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
