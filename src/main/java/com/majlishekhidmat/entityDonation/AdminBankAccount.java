package com.majlishekhidmat.entityDonation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private String paymentType; // BANK / UPI / GATEWAY

    // Bank fields
    private String bankName;
    private String accountNumber;
    private String ifsc;

    // UPI fields
    private String upiId;
    private String staticQrImageUrl;

    // Gateway fields
    private String gateway;
    private String keyId;
    private String keySecret;

    private boolean active;
    private String addedByAdmin;
}
