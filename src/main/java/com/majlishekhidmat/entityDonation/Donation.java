package com.majlishekhidmat.entityDonation;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donorName;
    private String donorEmail;
    private Double amount;
    private String currency;

    private String orderId;       
    private String paymentId;     
    private String signature;     
    private String status;        
    private String method;        

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    private Long campaignId;

    // Lazy loading ke wajah se direct serialization issue hota
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminBankAccount targetAccount;

    @Transient
    private String targetAccountId; // DTO mapping ke liye
}
