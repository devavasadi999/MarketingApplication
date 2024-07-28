package com.example.marketingapplication.common.emailutils;

import java.util.Map;

public interface IEmailService {
    Map<String, String> sendEmail(EmailDTO emailDTO);
}
