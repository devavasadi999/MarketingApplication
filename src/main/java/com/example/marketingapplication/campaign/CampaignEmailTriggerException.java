package com.example.marketingapplication.campaign;

import com.example.marketingapplication.common.CustomException;

//special exception class for the scenario when the send email is already processed previously for the campaign
public class CampaignEmailTriggerException extends CustomException {
    public CampaignEmailTriggerException(String message) {
        super(message);
    }
}
