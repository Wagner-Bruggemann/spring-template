package com.namgrengaw.template.application.adapters.out.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namgrengaw.template.application.adapters.out.mail.dto.EmailRequestDto;
import com.namgrengaw.template.application.config.mail.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailProperties emailProperties;

    public void sendSimpleEmail(EmailRequestDto emailRequestDto) {
        emailSender
                .to(emailRequestDto.getTo())
                .withSubject(emailRequestDto.getSubject())
                .withMessage(emailRequestDto.getBody())
                .send(emailProperties);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailRequestDto dto = new ObjectMapper().readValue(emailRequestJson, EmailRequestDto.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .to(dto.getTo())
                    .withSubject(dto.getSubject())
                    .withMessage(dto.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing attachment!", e);
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }

}
