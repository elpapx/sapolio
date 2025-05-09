package com.pichanga.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailRequest;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
@Slf4j
public class EmailService {

    private static final String EMAIL_TEMPLATE_NAME = "PichangaTemplate";
    private static final String SOURCE_EMAIL_NAME = "no-reply@pichangape.com";
    private static final String EMAIL_ACTIVATION_LINK = "http://localhost:8080/confirm?token=";
    private final SesClient sesClient;

    @Autowired
    public EmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Async
    public void send(String firstName, String destinationEmail, String token) {
        log.info("Sending email to: {}", destinationEmail);
        SendTemplatedEmailRequest request = SendTemplatedEmailRequest.builder()
                .destination(Destination.builder()
                        .toAddresses(destinationEmail)
                        .build())
                .template(EMAIL_TEMPLATE_NAME)
                .templateData(buildEmail(firstName, EMAIL_ACTIVATION_LINK + token))
                .source(SOURCE_EMAIL_NAME)
                .build();


        try {
            SendTemplatedEmailResponse response = sesClient.sendTemplatedEmail(request);
            log.info("Response from SES: {}", response);
        } catch (SesException e) {
            log.error("Failed to send the message", e);
            throw e;
        }
    }

    private String buildEmail(String name, String activationLink) {
        return String.format("{\"name\":\"%s\", \"confirmation_link\":\"%s\", \"current_year\":\"%d\"}",
                name, activationLink, java.time.LocalDate.now().getYear());
    }
}
