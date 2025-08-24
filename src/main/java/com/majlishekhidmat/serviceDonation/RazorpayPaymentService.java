package com.majlishekhidmat.serviceDonation;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.majlishekhidmat.dtoDonation.CreateOrderRequest;
import com.majlishekhidmat.dtoDonation.CreateOrderResponse;
import com.majlishekhidmat.dtoDonation.VerifyPaymentRequest;
import com.majlishekhidmat.dtoDonation.VerifyPaymentResponse;
import com.majlishekhidmat.entityDonation.AdminBankAccount;
import com.majlishekhidmat.entityDonation.Donation;
import com.majlishekhidmat.repositoryDonation.AdminBankAccountRepository;
import com.majlishekhidmat.repositoryDonation.DonationRepository;
import com.majlishekhidmat.utilDonation.SignatureUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService implements PaymentService {

    private final AdminBankAccountRepository bankRepo;
    private final DonationRepository donationRepo;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest req) {

        AdminBankAccount account = bankRepo.findById(req.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

        if (!"GATEWAY".equals(account.getPaymentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Selected account is not a gateway account for Razorpay payment");
        }

        if (!account.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected account is inactive");
        }

        RazorpayClient client;
        try {
            client = new RazorpayClient(account.getKeyId(), account.getKeySecret());
        } catch (RazorpayException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to initialize Razorpay client");
        }

        long amountPaise = Math.round(req.getAmount() * 100);
        String receipt = "don_" + System.currentTimeMillis();

        JSONObject options = new JSONObject();
        options.put("amount", amountPaise);
        options.put("currency", "INR");
        options.put("receipt", receipt);
        options.put("payment_capture", 1);

        try {
            Order order = client.orders.create(options);

            Donation donation = Donation.builder()
                    .donorName(req.getDonorName())
                    .amount(req.getAmount())
                    .orderId(order.get("id"))
                    .status("CREATED")
                    .method(req.getMethod())
                    .createdAt(LocalDateTime.now())
                    .targetAccount(account)
                    .build();

            donationRepo.save(donation);

            return new CreateOrderResponse(
                    order.get("id"),
                    account.getKeyId(),
                    amountPaise,
                    "INR",
                    receipt,
                    donation.getId()
            );

        } catch (RazorpayException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Razorpay order creation failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest req) {

        Donation donation = donationRepo.findById(req.getDonationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Donation not found"));

        AdminBankAccount account = donation.getTargetAccount();
        if (account == null || account.getKeySecret() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank account invalid");

        boolean valid = SignatureUtil.verifyRazorpaySignature(
                req.getRazorpayOrderId(), req.getRazorpayPaymentId(),
                account.getKeySecret(), req.getRazorpaySignature());

        if (valid) {
            donation.setPaymentId(req.getRazorpayPaymentId());
            donation.setSignature(req.getRazorpaySignature());
            donation.setStatus("SUCCESS");
            donation.setPaidAt(LocalDateTime.now());
            donationRepo.save(donation);
            return new VerifyPaymentResponse(true, "SUCCESS", "Payment verified");
        } else {
            donation.setStatus("FAILED");
            donationRepo.save(donation);
            return new VerifyPaymentResponse(false, "FAILED", "Signature mismatch");
        }
    }
}
