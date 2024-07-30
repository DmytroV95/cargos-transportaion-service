package com.varukha.emailservice.repository;

import com.varukha.emailservice.model.Email;
import com.varukha.emailservice.model.enums.EmailStatus;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmailRepository extends ElasticsearchRepository<Email, String> {
    List<Email> findByStatus(EmailStatus status);
}
