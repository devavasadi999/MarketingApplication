package com.example.marketingapplication.common.EmailUtils;

import com.example.marketingapplication.common.CustomException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService implements IEmailService {

    @Override
    public Map<String, String> sendEmail(EmailDTO emailDTO) {
        if(emailDTO.getBody() == null || emailDTO.getSubject() == null || emailDTO.getFromEmail() == null ||
                CollectionUtils.isEmpty(emailDTO.getToEmails())){
            throw new CustomException("Mandatory fields for sending an email are missing.");
        }

        Map<String, String> emailStatusMap = new HashMap<>();
        List<String> validEmails = validateEmails(emailDTO.getToEmails(), emailStatusMap);

        emailDTO.setToEmails(validEmails);
        sendToValidEmails(emailDTO);

        for(String email : validEmails){
            emailStatusMap.put(email, "Email Sent");
        }
        return emailStatusMap;
    }

    //validates the received emails, adds the invalid emails to email status map and returns valid emails
    private List<String> validateEmails(List<String> emails, Map<String, String> emailStatusMap){
        List<String> validEmails = new ArrayList<>();

        for(String singleEmail : emails){
            if(!EmailValidator.getInstance().isValid(singleEmail)){
                emailStatusMap.put(singleEmail, "Invalid Email");
            } else {
                validEmails.add(singleEmail);
            }
        }

        return validEmails;
    }

    private void sendToValidEmails(EmailDTO emailDTO){
        //mock method to send email using an email protocol
    }
}
