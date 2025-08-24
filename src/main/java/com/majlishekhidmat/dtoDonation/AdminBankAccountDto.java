package com.majlishekhidmat.dtoDonation;

import lombok.Data;

@Data
public class AdminBankAccountDto {
    private Long id;
    private String label;
    private String paymentType;

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
