

package com.majlishekhidmat.serviceV2;

import com.majlishekhidmat.dtoV2.CampaignDto;
import java.util.List;

public interface CampaignService {
    CampaignDto createCampaign(CampaignDto dto);
    CampaignDto updateCampaign(Long id, CampaignDto dto);
    void deleteCampaign(Long id);
    CampaignDto getCampaignById(Long id);
    List<CampaignDto> getAllCampaigns();
}

