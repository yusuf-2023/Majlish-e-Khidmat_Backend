package com.majlishekhidmat.serviceDonation;

import com.majlishekhidmat.dtoV2.DonationDto;

import com.majlishekhidmat.repositoryDonation.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonationUpdateService {

    private final DonationRepository donationRepo;

    @Transactional
    public void updateDonationStatus(DonationDto dto) {
        donationRepo.findByOrderId(dto.getOrderId()).ifPresent(d -> {
            d.setPaymentId(dto.getPaymentId());
            d.setStatus(dto.getStatus());
            if ("SUCCESS".equals(dto.getStatus())) {
                d.setPaidAt(LocalDateTime.now());
            }
            donationRepo.save(d);
        });
    }
}
