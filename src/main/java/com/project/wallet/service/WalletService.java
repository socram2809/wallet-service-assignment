package com.project.wallet.service;

import com.project.wallet.domain.Wallet;
import com.project.wallet.repository.WalletRepository;
import com.project.wallet.vo.WalletBalanceResponseVO;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletResponseVO create(WalletCreateRequestVO walletCreateRequest){
        log.info("Creating wallet for user: {}", walletCreateRequest.getUserIdentification());

        Wallet existingWallet = walletRepository.findByUserIdentification(walletCreateRequest.getUserIdentification());

        if(existingWallet != null){
            String error = "Wallet already exists for user: " + walletCreateRequest.getUserIdentification();
            log.error(error);
            throw new IllegalArgumentException(error);
        }

        Wallet wallet = new Wallet(walletCreateRequest.getUserIdentification());

        Wallet newWallet = walletRepository.save(wallet);

        WalletResponseVO walletResponse = WalletResponseVO.from(newWallet);

        log.info("Wallet created successfully: {}", walletResponse);

        return walletResponse;
    }

    public WalletBalanceResponseVO retrieveBalance(Long id) {
        log.info("Retrieving balance for wallet with id: {}", id);

        Wallet wallet;
        try {
            wallet = walletRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Wallet not found with id: " + id));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw e;
        }

        WalletBalanceResponseVO walletBalanceResponseVO = WalletBalanceResponseVO.from(wallet);

        log.info("Retrieved balance: {} for wallet with id: {}", walletBalanceResponseVO.getBalance(), id);

        return walletBalanceResponseVO;
    }

    public WalletResponseVO deposit(Long id, BigDecimal amount){
        log.info("Depositing {} to wallet with id: {}", amount, id);

        Wallet wallet;
        try {
            wallet = walletRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Wallet not found with id: " + id));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw e;
        }

        wallet.depositFunds(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        WalletResponseVO walletResponse = WalletResponseVO.from(updatedWallet);

        log.info("Deposit successful. Updated wallet: {}", walletResponse);

        return walletResponse;
    }

    public WalletResponseVO withdraw(Long id, BigDecimal amount){
        log.info("Withdrawing {} from wallet with id: {}", amount, id);

        Wallet wallet;
        try {
            wallet = walletRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Wallet not found with id: " + id));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw e;
        }

        wallet.withdrawFunds(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        WalletResponseVO walletResponse = WalletResponseVO.from(updatedWallet);

        log.info("Withdrawal successful. Updated wallet: {}", walletResponse);

        return walletResponse;
    }
}
