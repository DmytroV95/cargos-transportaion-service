package com.varukha.emailservice.model;

import com.varukha.emailservice.model.enums.EmailStatus;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "emails")
public class Email {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String to;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private EmailStatus status;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Integer)
    private Integer attemptCount = 0;

    @Field(type = FieldType.Date)
    private Instant lastAttemptTime;
}
