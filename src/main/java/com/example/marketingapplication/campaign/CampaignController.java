package com.example.marketingapplication.campaign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    private final ICampaignService campaignService;

    public CampaignController(ICampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCampaignsByClientId(@RequestParam(name = "client_id") Long clientId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("campaigns", campaignService.getCampaignsByClientId(clientId));
        return ResponseEntity.ok(resultMap);
    }

    @PostMapping
    public ResponseEntity<CampaignDTO> createCampaign(@RequestBody CampaignDTO campaignDTO) {
        return ResponseEntity.ok(campaignService.saveCampaign(campaignDTO));
    }

    @PostMapping("/send_email")
    public Map<String, String> sendEmail(@RequestBody CampaignDTO campaignDTO) {
        return campaignService.sendEmail(campaignDTO.getId());
    }
}

