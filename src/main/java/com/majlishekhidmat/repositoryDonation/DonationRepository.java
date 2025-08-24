package com.majlishekhidmat.repositoryDonation;

// repository/DonationRepository.java

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.majlishekhidmat.entityDonation.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
  Optional<Donation> findByOrderId(String orderId);

   @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d")
    Long sumAmount();
}

