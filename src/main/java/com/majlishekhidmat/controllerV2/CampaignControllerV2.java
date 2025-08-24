package com.majlishekhidmat.controllerV2;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.majlishekhidmat.dtoV2.CampaignDto;
import com.majlishekhidmat.serviceV2.CampaignService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CampaignControllerV2 {

    private final CampaignService campaignService;

    // ADMIN can create campaigns
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignDto> createCampaign(@RequestBody CampaignDto dto) {
        return ResponseEntity.ok(campaignService.createCampaign(dto));
    }

    // ADMIN or USER can fetch a campaign by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CampaignDto> getCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    // ADMIN can fetch all campaigns
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    // ADMIN can update a campaign
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable Long id, @RequestBody CampaignDto dto) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, dto));
    }

    // ADMIN can delete a campaign
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.ok("Campaign deleted successfully");
    }
}
