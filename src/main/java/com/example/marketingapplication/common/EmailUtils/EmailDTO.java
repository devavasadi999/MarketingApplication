package com.example.marketingapplication.common.emailutils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailDTO {
    private String fromEmail;

    private List<String> toEmails;

    private String subject;

    private String body;

    public EmailDTO(String fromEmail, List<String> toEmails, String subject, String body) {
        this.fromEmail = fromEmail;
        this.toEmails = toEmails;
        this.subject = subject;
        this.body = body;
    }
}
