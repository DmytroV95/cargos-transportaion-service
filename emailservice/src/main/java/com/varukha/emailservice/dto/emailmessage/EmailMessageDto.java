package com.varukha.emailservice.dto.emailmessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmailMessageDto implements Serializable {
    @NotBlank(message = "Recipient email address must not be blank")
    @Email(message = "Invalid email address")
    private String to;

    @NotBlank(message = "Email subject must not be blank")
    @Size(max = 255, message = "Email subject must not exceed 255 characters")
    private String subject;

    @NotBlank(message = "Email content must not be blank")
    private String content;
}
