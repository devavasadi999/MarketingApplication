package com.example.marketingapplication.campaign;

import com.example.marketingapplication.common.ApplicationContextProvider;
import com.example.marketingapplication.common.CustomException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.util.CollectionUtils;

public class CampaignListener {
    @PrePersist
    public void prePersist(Campaign campaign) {
        setStatus(campaign);
        CampaignDTO campaignDTO = ApplicationContextProvider.getBean(CampaignMapper.class).map(campaign);
        mandatoryFieldsMustBePresent(campaignDTO);
        nameMustBeUnique(campaignDTO);
    }

    private void nameMustBeUnique(CampaignDTO campaignDTO) {
        ICampaignService campaignService = ApplicationContextProvider.getBean(ICampaignService.class);
        if(campaignDTO.getId()!=null && campaignService.getById(campaignDTO.getId()).getName().equals(campaignDTO.getName())){
            return;
        }

        if(ApplicationContextProvider.getBean(ICampaignService.class).countByName(campaignDTO.getName()) > 0) {
            throw new CustomException("Duplicate Campaign name");
        }
    }

    private void setStatus(Campaign campaign) {
        if(campaign.getStatus()==null){
            campaign.setStatus(CampaignStatusEnum.DRAFT);
        }
    }

    private void mandatoryFieldsMustBePresent(CampaignDTO campaignDTO) {
        if(campaignDTO.getStatus() == null || campaignDTO.getName() == null || campaignDTO.getSubject() == null||
            campaignDTO.getEmailBody() == null || campaignDTO.getClientId() == null || CollectionUtils.isEmpty(campaignDTO.getSubscribers())){
            throw new CustomException("Mandatory fields are missing.");
        }
    }

    @PreUpdate
    private void postUpdate(Campaign campaign) {
        CampaignDTO campaignDTO = ApplicationContextProvider.getBean(CampaignMapper.class).map(campaign);
        mandatoryFieldsMustBePresent(campaignDTO);
        nameMustBeUnique(campaignDTO);
    }
}
