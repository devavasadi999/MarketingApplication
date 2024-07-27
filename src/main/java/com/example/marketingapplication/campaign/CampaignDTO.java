package com.example.marketingapplication.campaign;

import com.example.marketingapplication.common.CommonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CampaignDTO extends CommonDTO {

    private String name;

    private String subject;

    private CampaignStatusEnum status;

    @JsonProperty("email_body")
    private String emailBody;

    @JsonProperty("subscribers")
    private List<String> subscribers;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("client_email")
    private String clientEmail;
}
