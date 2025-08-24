package com.majlishekhidmat.controllerDonation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.majlishekhidmat.dtoDonation.AdminBankAccountDto;
import com.majlishekhidmat.entityDonation.AdminBankAccount;
import com.majlishekhidmat.repositoryDonation.AdminBankAccountRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/banks")
@RequiredArgsConstructor
public class AdminBankController {

    private final AdminBankAccountRepository repo;

    // Add bank/account/UPI/gateway
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminBankAccountDto> create(
            @Valid @RequestBody AdminBankAccountDto dto,
            @RequestParam String adminName) {

        AdminBankAccount entity = AdminBankAccount.builder()
                .label(dto.getLabel())
                .paymentType(dto.getPaymentType())
                .bankName(dto.getBankName())
                .accountNumber(dto.getAccountNumber())
                .ifsc(dto.getIfsc())
                .upiId(dto.getUpiId())
                .staticQrImageUrl(dto.getStaticQrImageUrl())
                .gateway(dto.getGateway())
                .keyId(dto.getKeyId())
                .keySecret(dto.getKeySecret())
                .active(dto.isActive())
                .addedByAdmin(adminName)
                .build();

        repo.save(entity);
        dto.setId(entity.getId());
        dto.setAddedByAdmin(adminName);
        return ResponseEntity.ok(dto);
    }

    // List all accounts (admin view)
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminBankAccountDto>> listAll() {
        List<AdminBankAccountDto> list = repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // List only active accounts (USER + ADMIN view)
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<AdminBankAccountDto>> listActive() {
        List<AdminBankAccountDto> list = repo.findAll().stream()
                .filter(AdminBankAccount::isActive)
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Update account
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminBankAccountDto> update(@PathVariable Long id, @RequestBody AdminBankAccountDto dto) {
        AdminBankAccount acc = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        acc.setLabel(dto.getLabel());
        acc.setPaymentType(dto.getPaymentType());
        acc.setBankName(dto.getBankName());
        acc.setAccountNumber(dto.getAccountNumber());
        acc.setIfsc(dto.getIfsc());
        acc.setUpiId(dto.getUpiId());
        acc.setStaticQrImageUrl(dto.getStaticQrImageUrl());
        acc.setGateway(dto.getGateway());
        acc.setKeyId(dto.getKeyId());
        acc.setKeySecret(dto.getKeySecret());
        acc.setActive(dto.isActive());
        repo.save(acc);

        dto.setId(acc.getId());
        dto.setAddedByAdmin(acc.getAddedByAdmin());
        return ResponseEntity.ok(dto);
    }

    // Delete account
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        AdminBankAccount acc = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        repo.delete(acc);
        return ResponseEntity.ok().build();
    }

    // Helper method to convert entity -> DTO
    private AdminBankAccountDto toDto(AdminBankAccount acc) {
        AdminBankAccountDto d = new AdminBankAccountDto();
        d.setId(acc.getId());
        d.setLabel(acc.getLabel());
        d.setPaymentType(acc.getPaymentType());
        d.setBankName(acc.getBankName());
        d.setAccountNumber(acc.getAccountNumber());
        d.setIfsc(acc.getIfsc());
        d.setUpiId(acc.getUpiId());
        d.setStaticQrImageUrl(acc.getStaticQrImageUrl());
        d.setGateway(acc.getGateway());
        d.setKeyId(acc.getKeyId());
        d.setKeySecret(acc.getKeySecret());
        d.setActive(acc.isActive());
        d.setAddedByAdmin(acc.getAddedByAdmin());
        return d;
    }
}
