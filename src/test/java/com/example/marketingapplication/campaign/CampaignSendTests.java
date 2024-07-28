package com.example.marketingapplication.campaign;

import com.example.marketingapplication.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignSendTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    public void testSendCampaign() throws Exception {
        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Campaign to Send");
        campaign.setSubject("Subject");
        campaign.setStatus(CampaignStatusEnum.DRAFT);
        campaign.setEmailBody("This is the email body.");
        Client client = new Client();
        client.setId(1L);
        client.setEmail("client@example.com");
        campaign.setClient(client);
        campaign.setSubscribers(Arrays.asList("email1@example.com", "email2@example.com"));

        Campaign campaignSpy = Mockito.spy(campaign);

        CampaignDTO request = new CampaignDTO();
        request.setId(1L);

        Mockito.when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaignSpy));

        mockMvc.perform(post("/api/campaign/send_email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['"+campaign.getSubscribers().getFirst()+"']")
                        .value("Email Sent"))
                .andExpect(jsonPath("$.['"+campaign.getSubscribers().get(1)+"']")
                        .value("Email Sent"));

        Mockito.verify(campaignRepository, Mockito.times(2)).save(Mockito.any());
    }
}

