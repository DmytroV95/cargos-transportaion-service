package com.varukha.emailservice.controller;

import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;
import com.varukha.emailservice.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email management",
        description = "Endpoints for managing emails")
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send email",
            description = "Send message to recipient email address")
    public void sendEmail(@RequestBody @Valid EmailMessageDto emailMessageDto) {
        emailService.handleEmailMessage(emailMessageDto);
    }
}
