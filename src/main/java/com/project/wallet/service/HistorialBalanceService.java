package com.project.wallet.service;

import com.project.wallet.domain.HistoricalBalance;
import com.project.wallet.domain.Wallet;
import com.project.wallet.repository.HistoricalBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistorialBalanceService {

    private final HistoricalBalanceRepository historicalBalanceRepository;

    public void create(Wallet wallet) {
        log.info("Saving historical balance for wallet: {}", wallet);

        HistoricalBalance historicalBalance = new HistoricalBalance(wallet);

        historicalBalanceRepository.save(historicalBalance);

        log.info("Historical balance saved successfully for wallet: {}", wallet);
    }

    public HistoricalBalance findLastByWalletIdAndTransactionDateLessThanEqual(Long walletId, LocalDateTime date) {
        log.info("Finding last historical balance for wallet ID: {} before date: {}", walletId, date);

        HistoricalBalance historicalBalance = historicalBalanceRepository.findTopByWalletIdAndTransactionDateLessThanEqualOrderByTransactionDateDesc(walletId, date);

        if (historicalBalance == null) {
            String error = "No historical balance found for wallet ID: " + walletId + " before date: " + date;
            log.error(error);
            throw new NoSuchElementException(error);
        }

        log.info("Found historical balance: {}", historicalBalance);
        return historicalBalance;
    }
}
