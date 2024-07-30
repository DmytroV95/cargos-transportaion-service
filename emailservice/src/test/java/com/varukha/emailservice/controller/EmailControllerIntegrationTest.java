package com.varukha.emailservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.emailservice.dto.emailmessage.EmailMessageDto;
import com.varukha.emailservice.model.Email;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailControllerIntegrationTest {
    protected static MockMvc mockMvc;
    private static EmailMessageDto emailMessageDto;
    private static final String EMAILS_ENDPOINT = "/emails";
    private static final String ENV_FILE_PATH = "../../.env";
    private static String emailTo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) throws IOException {
        loadEnvVariables();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @BeforeEach
    void setUp() {
        emailMessageDto = new EmailMessageDto(
                emailTo,
                "Test Subject",
                "Test Content");
    }

    @AfterEach
    public void cleanUp() {
        elasticsearchOperations.indexOps(Email.class).delete();
    }

    @Test
    public void sendEmail_Success() throws Exception {
        mockMvc.perform(post(EMAILS_ENDPOINT + "/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMessageDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendEmailFailure() throws Exception {
        emailMessageDto.setTo(null);

        mockMvc.perform(post(EMAILS_ENDPOINT + "/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMessageDto)))
                .andExpect(status().isBadRequest());
    }

    private static void loadEnvVariables() throws IOException {
        try (FileInputStream envProps = new FileInputStream(ENV_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(envProps);
            emailTo = properties.getProperty("TO_EMAIL");
        }
    }
}
