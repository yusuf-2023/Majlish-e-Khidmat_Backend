package com.majlishekhidmat.dtoDonation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyPaymentResponse {
    private boolean valid;
    private String status; // SUCCESS/FAILED
    private String message;
}
