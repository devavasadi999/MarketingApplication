package com.example.marketingapplication.campaign.IntegrationTests;

import com.example.marketingapplication.campaign.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignAPIIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICampaignService campaignService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAPIs() throws Exception {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setName("New Campaign");
        campaignDTO.setSubject("Subject");
        campaignDTO.setEmailBody("This is the email body.");
        campaignDTO.setClientId(1L);
        campaignDTO.setSubscribers(Arrays.asList("email1@example.com", "email2@example.com"));

        Long campaignId = null;
        try {
            MvcResult result = mockMvc.perform(post("/api/campaign")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(campaignDTO)))
                    .andExpect(status().isOk())
                    .andReturn();

            // Extract JSON from response
            String jsonResponse = result.getResponse().getContentAsString();

            // Parse JSON to extract the ID
            campaignId = objectMapper.readTree(jsonResponse).get("id").asLong();

            // Assert the ID is not null
            assertThat(campaignId).isNotNull();

            //retrieve the created object using get API
            testGetCampaignAPI(1L);

            //test send mail for the created campaign
            testSendMail(campaignId);
        } finally {
            //delete the created object
            campaignService.deleteById(campaignId);
        }
    }

    private void testGetCampaignAPI(Long clientId) throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/campaign").param("client_id", clientId.toString()))
                .andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        JsonNode campaignsNode = objectMapper.readTree(jsonResponse).get("campaigns");
        assertThat(campaignsNode).isNotNull();
        assertThat(campaignsNode.isArray()).isTrue();
        assertThat(campaignsNode.size()).isGreaterThanOrEqualTo(1);
    }

    private void testSendMail(Long campaignId) throws Exception {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setId(campaignId);

        MvcResult mvcResult = mockMvc.perform(post("/api/campaign/send_email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campaignDTO)))
                .andExpect(status().isOk()).andReturn();

        CampaignDTO updatedCampaign = campaignService.getById(campaignId);
        assertThat(updatedCampaign).isNotNull();
        assertThat(updatedCampaign.getStatus()).isEqualTo(CampaignStatusEnum.SENT);
    }
}
