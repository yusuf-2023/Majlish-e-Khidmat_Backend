package com.majlishekhidmat.serviceDonation;

import com.majlishekhidmat.dtoDonation.AdminBankAccountDto;
import com.majlishekhidmat.entityDonation.AdminBankAccount;
import com.majlishekhidmat.repositoryDonation.AdminBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBankService {

    private final AdminBankAccountRepository repo;

    // ==========================
    // Create a new bank account
    // ==========================
    public AdminBankAccountDto createBank(AdminBankAccountDto dto) {
        AdminBankAccount entity = AdminBankAccount.builder()
                .label(dto.getLabel())
                .bankName(dto.getBankName())
                .upiId(dto.getUpiId())
                .active(dto.isActive())
                .gateway(dto.getGateway())
                .keyId(dto.getKeyId())
                .keySecret(dto.getKeySecret())
                .staticQrImageUrl(dto.getStaticQrImageUrl())
                .build();

        repo.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    // ==========================
    // Update existing bank account
    // ==========================
    public AdminBankAccountDto updateBank(Long id, AdminBankAccountDto dto) {
        AdminBankAccount entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        entity.setLabel(dto.getLabel());
        entity.setBankName(dto.getBankName());
        entity.setUpiId(dto.getUpiId());
        entity.setActive(dto.isActive());
        entity.setGateway(dto.getGateway());
        entity.setKeyId(dto.getKeyId());
        entity.setKeySecret(dto.getKeySecret());
        entity.setStaticQrImageUrl(dto.getStaticQrImageUrl());

        repo.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    // ==========================
    // Delete bank account
    // ==========================
    public void deleteBank(Long id) {
        AdminBankAccount entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        repo.delete(entity);
    }

    // ==========================
    // List all active bank accounts
    // ==========================
    @Transactional(readOnly = true)
    public List<AdminBankAccountDto> listActiveBanks() {
        return repo.findByActiveTrue().stream().map(acc -> {
            AdminBankAccountDto dto = new AdminBankAccountDto();
            dto.setId(acc.getId());
            dto.setLabel(acc.getLabel());
            dto.setBankName(acc.getBankName());
            dto.setUpiId(acc.getUpiId());
            dto.setActive(acc.isActive());
            dto.setGateway(acc.getGateway());
            dto.setKeyId(acc.getKeyId());
            dto.setKeySecret(acc.getKeySecret());
            dto.setStaticQrImageUrl(acc.getStaticQrImageUrl());
            return dto;
        }).collect(Collectors.toList());
    }

    // ==========================
    // List all bank accounts (active + inactive)
    // ==========================
    @Transactional(readOnly = true)
    public List<AdminBankAccountDto> listAllBanks() {
        return repo.findAll().stream().map(acc -> {
            AdminBankAccountDto dto = new AdminBankAccountDto();
            dto.setId(acc.getId());
            dto.setLabel(acc.getLabel());
            dto.setBankName(acc.getBankName());
            dto.setUpiId(acc.getUpiId());
            dto.setActive(acc.isActive());
            dto.setGateway(acc.getGateway());
            dto.setKeyId(acc.getKeyId());
            dto.setKeySecret(acc.getKeySecret());
            dto.setStaticQrImageUrl(acc.getStaticQrImageUrl());
            return dto;
        }).collect(Collectors.toList());
    }
}
