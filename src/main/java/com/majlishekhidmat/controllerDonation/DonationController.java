package com.majlishekhidmat.controllerDonation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.majlishekhidmat.dtoV2.DonationDto;
import com.majlishekhidmat.dtoDonation.CreateOrderRequest;
import com.majlishekhidmat.dtoDonation.CreateOrderResponse;
import com.majlishekhidmat.dtoDonation.VerifyPaymentRequest;
import com.majlishekhidmat.dtoDonation.VerifyPaymentResponse;
import com.majlishekhidmat.entityDonation.AdminBankAccount;
import com.majlishekhidmat.entityDonation.Donation;
import com.majlishekhidmat.repositoryDonation.DonationRepository;
import com.majlishekhidmat.serviceDonation.PaymentService;
import com.majlishekhidmat.serviceDonation.UpiQrService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DonationController {

    private final PaymentService paymentService;
    private final UpiQrService upiQrService;
    private final DonationRepository donationRepo;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/order")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest req) {
        try {
            if (req.getAmount() == null || req.getAmount() <= 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
            if (req.getDonorName() == null || req.getDonorName().isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Donor name required");
            if (req.getAccountId() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID is required");

            return ResponseEntity.ok(paymentService.createOrder(req));

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Order creation failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/verify")
    public ResponseEntity<VerifyPaymentResponse> verify(@RequestBody VerifyPaymentRequest req) {
        try {
            if (req.getDonationId() == null || req.getRazorpayOrderId() == null ||
                req.getRazorpayPaymentId() == null || req.getRazorpaySignature() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing payment details");
            }
            return ResponseEntity.ok(paymentService.verifyPayment(req));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Payment verification failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{donationId}/upi-link")
    public ResponseEntity<String> upiLink(@PathVariable Long donationId) {
        Donation d = donationRepo.findById(donationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not found"));

        AdminBankAccount account = d.getTargetAccount();
        if (account == null || account.getUpiId() == null || account.getUpiId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target account missing UPI ID");
        }

        String link = upiQrService.buildUpiUri(
                account.getUpiId(),
                account.getLabel() != null ? account.getLabel() : "Donation",
                d.getAmount(),
                "Donation " + d.getId()
        );
        return ResponseEntity.ok(link);
    }

    // ✅ DTO mapping ke sath GET all donations
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<DonationDto>> getAllDonations() {
        try {
            List<Donation> donations = donationRepo.findAll();

            List<DonationDto> dtoList = donations.stream().map(d -> {
                DonationDto dto = new DonationDto();
                dto.setId(d.getId());
                dto.setDonorName(d.getDonorName());
                dto.setDonorEmail(d.getDonorEmail());
                dto.setAmount(d.getAmount());
                dto.setCurrency(d.getCurrency());
                dto.setCampaignId(d.getCampaignId());
                dto.setPaymentId(d.getPaymentId());
                dto.setOrderId(d.getOrderId());
                dto.setStatus(d.getStatus());
                dto.setTargetAccountId(d.getTargetAccount() != null ? d.getTargetAccount().getId().toString() : null);
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching donations: " + e.getMessage());
        }
    }
}
