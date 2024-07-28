package com.example.marketingapplication.campaign.MockAPITests;

import com.example.marketingapplication.campaign.Campaign;
import com.example.marketingapplication.campaign.CampaignRepository;
import com.example.marketingapplication.campaign.CampaignStatusEnum;
import com.example.marketingapplication.client.Client;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignListTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    public void testListCampaigns() throws Exception {
        Campaign campaign1 = new Campaign();
        campaign1.setId(1L);
        campaign1.setName("Campaign 1");
        campaign1.setSubject("Subject 1");
        campaign1.setStatus(CampaignStatusEnum.DRAFT);
        campaign1.setEmailBody("Email body 1");
        Client client = new Client();
        client.setId(1L);
        client.setEmail("client1@example.com");
        campaign1.setClient(client);
        campaign1.setSubscribers(Arrays.asList("email1@example.com", "email2@example.com"));

        Campaign campaign2 = new Campaign();
        campaign2.setId(2L);
        campaign2.setName("Campaign 2");
        campaign2.setSubject("Subject 2");
        campaign2.setStatus(CampaignStatusEnum.SENT);
        campaign2.setEmailBody("Email body 2");
        campaign2.setClient(client);
        campaign2.setSubscribers(Arrays.asList("email3@example.com", "email4@example.com"));

        Mockito.when(campaignRepository.findByClientId(Mockito.eq(1L))).thenReturn(Arrays.asList(campaign1, campaign2));

        mockMvc.perform(get("/api/campaign").param("client_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaigns[0].id").value(campaign1.getId()))
                .andExpect(jsonPath("$.campaigns[0].name").value(campaign1.getName()))
                .andExpect(jsonPath("$.campaigns[0].subject").value(campaign1.getSubject()))
                .andExpect(jsonPath("$.campaigns[0].status").value(campaign1.getStatus().toString()))
                .andExpect(jsonPath("$.campaigns[0].email_body").value(campaign1.getEmailBody()))
                .andExpect(jsonPath("$.campaigns[0].client_id").value(client.getId()))
                .andExpect(jsonPath("$.campaigns[0].client_email").value(client.getEmail()))
                .andExpect(jsonPath("$.campaigns[0].subscribers", containsInAnyOrder(campaign1.getSubscribers().toArray())))
                .andExpect(jsonPath("$.campaigns[1].id").value(campaign2.getId()))
                .andExpect(jsonPath("$.campaigns[1].name").value(campaign2.getName()))
                .andExpect(jsonPath("$.campaigns[1].subject").value(campaign2.getSubject()))
                .andExpect(jsonPath("$.campaigns[1].status").value(campaign2.getStatus().toString()))
                .andExpect(jsonPath("$.campaigns[1].email_body").value(campaign2.getEmailBody()))
                .andExpect(jsonPath("$.campaigns[1].client_id").value(client.getId()))
                .andExpect(jsonPath("$.campaigns[1].client_email").value(client.getEmail()))
                .andExpect(jsonPath("$.campaigns[1].subscribers", containsInAnyOrder(campaign2.getSubscribers().toArray())));
    }
}
