package com.majlishekhidmat.dtoV2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationDto {

    private Long id;
    private String donorName;
    private String donorEmail;
    private Double amount;
    private String currency;
    private Long campaignId;
    private String targetAccountId; // AdminBankAccount ka ID
    private String paymentId;
    private String orderId;
    private String status;
}
