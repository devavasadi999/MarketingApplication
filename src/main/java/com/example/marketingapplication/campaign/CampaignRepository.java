package com.example.marketingapplication.campaign;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByClientId(Long clientId);

    long countByName(String name);
}

