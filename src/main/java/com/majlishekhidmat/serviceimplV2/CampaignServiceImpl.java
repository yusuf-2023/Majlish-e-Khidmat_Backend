package com.majlishekhidmat.serviceimplV2;


import com.majlishekhidmat.dtoV2.CampaignDto;
import com.majlishekhidmat.entityV2.Campaign;
import com.majlishekhidmat.repositoryV2.CampaignRepository;
import com.majlishekhidmat.serviceV2.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    @Override
    public CampaignDto createCampaign(CampaignDto dto) {
        Campaign campaign = Campaign.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .targetAmount(dto.getTargetAmount())
                .status(dto.getStatus())
                .build();

        Campaign saved = campaignRepository.save(campaign);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public CampaignDto updateCampaign(Long id, CampaignDto dto) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setTitle(dto.getTitle());
        campaign.setDescription(dto.getDescription());
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        campaign.setTargetAmount(dto.getTargetAmount());
        campaign.setStatus(dto.getStatus());

        campaignRepository.save(campaign);
        dto.setId(campaign.getId());
        return dto;
    }

    @Override
    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }

    @Override
    public CampaignDto getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        CampaignDto dto = new CampaignDto();
        dto.setId(campaign.getId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setTargetAmount(campaign.getTargetAmount());
        dto.setStatus(campaign.getStatus());
        return dto;
    }

    @Override
    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll().stream().map(c -> {
            CampaignDto dto = new CampaignDto();
            dto.setId(c.getId());
            dto.setTitle(c.getTitle());
            dto.setDescription(c.getDescription());
            dto.setStartDate(c.getStartDate());
            dto.setEndDate(c.getEndDate());
            dto.setTargetAmount(c.getTargetAmount());
            dto.setStatus(c.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}

