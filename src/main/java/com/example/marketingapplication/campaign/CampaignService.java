package com.example.marketingapplication.campaign;

import com.example.marketingapplication.common.CustomException;
import com.example.marketingapplication.common.emailutils.EmailDTO;
import com.example.marketingapplication.common.emailutils.IEmailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CampaignService implements ICampaignService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CampaignRepository campaignRepository;

    private final CampaignMapper campaignMapper;

    private final IEmailService emailService;

    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper, IEmailService emailService) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.emailService = emailService;
    }

    @Override
    public List<CampaignDTO> getCampaignsByClientId(Long clientId) {
        return campaignMapper.map(campaignRepository.findByClientId(clientId));
    }

    @Override
    public CampaignDTO saveCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = campaignRepository.save(campaignMapper.map(campaignDTO));
        entityManager.detach(campaign);
        return getById(campaign.getId());
    }

    @Override
    @Transactional
    public CampaignDTO getById(Long id) {
        return campaignMapper.map(campaignRepository.findById(id).orElse(null));
    }

    @Override
    public Map<String, String> sendEmail(Long campaignId) {
        if(campaignId == null){
            throw new CustomException("Invalid Request");
        }
        CampaignDTO campaignDTO = campaignMapper.map(campaignRepository.findById(campaignId).orElse(null));
        if(campaignDTO == null){
            throw new CustomException("Invalid campaign.");
        }

        try {
            CampaignEmailLocker.acquireLock(campaignId);
            validateStatus(campaignDTO);
            updateStatus(campaignDTO, CampaignStatusEnum.SENDING);

            EmailDTO emailDTO = new EmailDTO(campaignDTO.getClientEmail(), campaignDTO.getSubscribers(),
                    campaignDTO.getSubject(), campaignDTO.getEmailBody());

            Map<String, String> emailStatusMap = emailService.sendEmail(emailDTO);
            updateStatus(campaignDTO, CampaignStatusEnum.SENT);
            return emailStatusMap;

        } catch(CampaignEmailTriggerException e){
            throw e;
        } catch(Throwable t){
            updateStatus(campaignDTO, CampaignStatusEnum.DRAFT);
            throw t;
        } finally {
            CampaignEmailLocker.releaseLock(campaignId);
        }
    }

    private void validateStatus(CampaignDTO campaignDTO) {
        if(campaignDTO.getStatus() != CampaignStatusEnum.DRAFT){
            throw new CampaignEmailTriggerException("Campaign status is not DRAFT.");
        }
    }

    private void updateStatus(CampaignDTO campaignDTO, CampaignStatusEnum newStatus) {
        campaignDTO.setStatus(newStatus);
        campaignRepository.save(campaignMapper.map(campaignDTO));
    }

    @Override
    public void deleteById(Long id) {
        campaignRepository.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long countByName(String name){
        return campaignRepository.countByName(name);
    }
}