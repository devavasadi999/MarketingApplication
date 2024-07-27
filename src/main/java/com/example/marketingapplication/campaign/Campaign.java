package com.example.marketingapplication.campaign;

import com.example.marketingapplication.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(CampaignListener.class)
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String subject;

    @Enumerated(EnumType.STRING)
    private CampaignStatusEnum status;

    @Column(name = "email_body")
    private String emailBody;

    @ElementCollection
    @CollectionTable(name = "campaign_subscriber", joinColumns = {@JoinColumn(name =
    "campaign_id")})
    @Column(name = "subscriber_email")
    private List<String> subscribers;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

