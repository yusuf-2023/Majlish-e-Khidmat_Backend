package com.majlishekhidmat.serviceV2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.majlishekhidmat.repository.UserRepository;
import com.majlishekhidmat.repositoryDonation.DonationRepository;
import com.majlishekhidmat.repositoryV2.CampaignRepository;
import com.majlishekhidmat.repositoryV2.FeedbackRepository;
import com.majlishekhidmat.repositoryV2.InventoryRepository;
import com.majlishekhidmat.repositoryV2.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;
    private final InventoryRepository inventoryRepository;
    private final FeedbackRepository feedbackRepository;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("activeVolunteers", volunteerRepository.count());
        stats.put("donationsCollected", donationRepository.sumAmount());
        stats.put("campaigns", campaignRepository.count());
        stats.put("inventoryItems", inventoryRepository.count());
        stats.put("feedbacks", feedbackRepository.count());
        return stats;
    }
}
