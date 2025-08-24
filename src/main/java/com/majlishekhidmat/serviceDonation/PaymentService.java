package com.majlishekhidmat.serviceDonation;

import com.majlishekhidmat.dtoDonation.CreateOrderRequest;
import com.majlishekhidmat.dtoDonation.CreateOrderResponse;
import com.majlishekhidmat.dtoDonation.VerifyPaymentRequest;
import com.majlishekhidmat.dtoDonation.VerifyPaymentResponse;

public interface PaymentService {
    CreateOrderResponse createOrder(CreateOrderRequest req);  // ✅ Change here
    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest req);
}
