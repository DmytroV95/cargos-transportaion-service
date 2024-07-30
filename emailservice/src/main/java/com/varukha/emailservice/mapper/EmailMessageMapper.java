package com.varukha.emailservice.mapper;

import com.varukha.emailservice.config.MapperConfig;
import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;
import com.varukha.emailservice.model.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for mapping email-related objects.
 */
@Mapper(config = MapperConfig.class)
public interface EmailMessageMapper {
    /**
     * Converts a EmailMessageDto to an Email entity.
     *
     * @param messageDto the EmailMessageDto to convert.
     * @return the corresponding Email entity.
     */
    @Mapping(target = "id", ignore = true)
    Email toModel(EmailMessageDto messageDto);
}
