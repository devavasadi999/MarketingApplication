package com.example.marketingapplication.campaign;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    @Mapping(source = "client.id", target="clientId")
    @Mapping(source = "client.email", target = "clientEmail")
    CampaignDTO map(Campaign campaign);

    @InheritInverseConfiguration
    Campaign map(CampaignDTO campaignDTO);

    List<CampaignDTO> map(List<Campaign> campaigns);
}
