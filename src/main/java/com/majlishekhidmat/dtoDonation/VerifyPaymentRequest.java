package com.majlishekhidmat.dtoDonation;

import lombok.Data;

@Data
public class VerifyPaymentRequest {
    private Long donationId;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
