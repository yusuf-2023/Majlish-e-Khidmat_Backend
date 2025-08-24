package com.majlishekhidmat.controllerDonation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.majlishekhidmat.dtoDonation.AdminBankAccountDto;
import com.majlishekhidmat.entityDonation.AdminBankAccount;
import com.majlishekhidmat.repositoryDonation.AdminBankAccountRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banks") // user-facing endpoint
@RequiredArgsConstructor
public class BankController {

    private final AdminBankAccountRepository repo;

    // List only active accounts (USER + ADMIN view)
    @GetMapping("/active")
    public ResponseEntity<List<AdminBankAccountDto>> listActiveBanks() {
        List<AdminBankAccountDto> list = repo.findAll().stream()
                .filter(AdminBankAccount::isActive)
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

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
