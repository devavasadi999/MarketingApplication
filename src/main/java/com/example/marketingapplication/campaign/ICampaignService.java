package com.example.marketingapplication.campaign;

import java.util.List;
import java.util.Map;

public interface ICampaignService {
    CampaignDTO saveCampaign(CampaignDTO campaignDTO);

    Map<String, String> sendEmail(Long campaignId);

    List<CampaignDTO> getCampaignsByClientId(Long clientId);

    CampaignDTO getById(Long id);

    long countByName(String name);
}
