package com.example.marketingapplication.campaign;

import com.example.marketingapplication.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignCreateTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    public void testCreateCampaign() throws Exception {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setName("New Campaign");
        campaignDTO.setSubject("Subject");
        campaignDTO.setEmailBody("This is the email body.");
        campaignDTO.setClientId(1L);
        campaignDTO.setSubscribers(Arrays.asList("email1@example.com", "email2@example.com"));

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName(campaignDTO.getName());
        campaign.setSubject(campaignDTO.getSubject());
        campaign.setStatus(CampaignStatusEnum.DRAFT);
        campaign.setEmailBody(campaignDTO.getEmailBody());
        campaign.setSubscribers(campaignDTO.getSubscribers());
        Client client = new Client();
        client.setId(campaignDTO.getClientId());
        client.setEmail("client_email@example.com");
        campaign.setClient(client);

        Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).thenReturn(campaign);
        Mockito.when(campaignRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(campaign));

        mockMvc.perform(post("/api/campaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(campaign.getId()))
                .andExpect(jsonPath("$.name").value(campaignDTO.getName()))
                .andExpect(jsonPath("$.subject").value(campaignDTO.getSubject()))
                .andExpect(jsonPath("$.status").value(CampaignStatusEnum.DRAFT.toString()))
                .andExpect(jsonPath("$.email_body").value(campaignDTO.getEmailBody()))
                .andExpect(jsonPath("$.subscribers", containsInAnyOrder(campaignDTO.getSubscribers().toArray())))
                .andExpect(jsonPath("$.client_id").value(campaignDTO.getClientId()))
                .andExpect(jsonPath("$.client_email").value(client.getEmail()));
    }
}

