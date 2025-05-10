package com.project.wallet.repository;

import com.project.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Long, Wallet> {
}
