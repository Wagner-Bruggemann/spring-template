package com.namgrengaw.template.application.adapters.out.mail.dto;

import java.util.Objects;

public class EmailRequestDto {

    private String to;
    private String subject;
    private String body;

    public EmailRequestDto() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        EmailRequestDto that = (EmailRequestDto) object;
        return Objects.equals(to, that.to) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, subject, body);
    }
}
