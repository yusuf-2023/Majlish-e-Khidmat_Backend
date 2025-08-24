package com.majlishekhidmat.repositoryDonation;

// repository/AdminBankAccountRepository.java

import com.majlishekhidmat.entityDonation.AdminBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminBankAccountRepository extends JpaRepository<AdminBankAccount, Long> {
  List<AdminBankAccount> findByActiveTrue();
}

