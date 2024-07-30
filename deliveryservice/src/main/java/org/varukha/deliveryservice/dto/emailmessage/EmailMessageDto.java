package org.varukha.deliveryservice.dto.emailmessage;

import java.io.Serializable;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record EmailMessageDto(
        String to,
        String subject,
        String content) implements Serializable {
}
