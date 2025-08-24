package com.majlishekhidmat.repositoryV2;

import com.majlishekhidmat.entityV2.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
