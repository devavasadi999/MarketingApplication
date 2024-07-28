package com.example.marketingapplication.campaign;

import java.util.HashSet;
import java.util.Set;

public class CampaignEmailLocker {
    private static Set<Long> campaignIds = new HashSet<>();

    public static void acquireLock(Long campaignId){
        if(campaignIds.contains(campaignId)){
            throw new CampaignEmailTriggerException("Previous request of send email is being processed. Try again after some time.");
        }
        campaignIds.add(campaignId);
    }

    public static void releaseLock(Long campaignId){
        campaignIds.remove(campaignId);
    }
}
