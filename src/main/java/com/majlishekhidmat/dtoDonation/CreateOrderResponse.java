package com.majlishekhidmat.dtoDonation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private String orderId;       // Razorpay order ID
    private String keyId;         // Razorpay key ID
    private long amountInPaise;   // Amount in paise
    private String currency;      // Currency (INR)
    private String receipt;       // Receipt string
    private Long donationId;      // Backend donation ID
}
