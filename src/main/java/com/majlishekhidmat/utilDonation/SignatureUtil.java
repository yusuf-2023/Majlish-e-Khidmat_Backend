package com.majlishekhidmat.utilDonation;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureUtil {

    private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);

    public static boolean verifyRazorpaySignature(String orderId, String paymentId, String secret, String signature) {
        try {
            // Correct payload
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));

            byte[] digest = mac.doFinal(payload.getBytes());

            // Convert to Hex
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String expectedHex = sb.toString();

            // Compare with Razorpay's signature
            return expectedHex.equals(signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // ⚡ Production-grade error handling
            log.error("Signature verification failed: {}", e.getMessage(), e);
            return false;
        }
    }
}
