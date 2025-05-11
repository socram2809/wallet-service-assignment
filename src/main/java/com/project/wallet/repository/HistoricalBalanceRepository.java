package com.project.wallet.repository;

import com.project.wallet.domain.HistoricalBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HistoricalBalanceRepository extends JpaRepository<HistoricalBalance, Long> {

    HistoricalBalance findTopByWalletIdAndTransactionDateLessThanEqualOrderByTransactionDateDesc(Long walletId, LocalDateTime date);
}
