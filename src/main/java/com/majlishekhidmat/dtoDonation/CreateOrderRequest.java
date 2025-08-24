package com.majlishekhidmat.dtoDonation;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long accountId;
    private Double amount;      // INR
    private String donorName;
    private String method;      // optional
}
