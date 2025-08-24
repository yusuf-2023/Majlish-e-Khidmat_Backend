package com.majlishekhidmat.controllerDonation;

import com.majlishekhidmat.repositoryDonation.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final DonationRepository donationRepo;

    @PostMapping("/razorpay")
    public ResponseEntity<Void> razorpayWebhook(@RequestBody Map<String, Object> payload,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {

        JSONObject json = new JSONObject(payload);
        String event = json.optString("event", "");
        if (event.startsWith("payment.")) {
            JSONObject payment = json.getJSONObject("payload")
                                     .getJSONObject("payment")
                                     .getJSONObject("entity");

            String orderId = payment.optString("order_id");
            String paymentId = payment.optString("id");
            String status = payment.optString("status"); // captured/failed/authorized

            donationRepo.findByOrderId(orderId).ifPresent(d -> {
                if ("captured".equalsIgnoreCase(status)) {
                    d.setPaymentId(paymentId);
                    d.setStatus("SUCCESS");
                    d.setPaidAt(LocalDateTime.now());
                    donationRepo.save(d);
                } else if ("failed".equalsIgnoreCase(status)) {
                    d.setStatus("FAILED");
                    donationRepo.save(d);
                }
            });
        }

        return ResponseEntity.ok().build();
    }
}
