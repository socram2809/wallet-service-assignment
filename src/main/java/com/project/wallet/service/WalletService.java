package com.project.wallet.service;

import com.project.wallet.domain.HistoricalBalance;
import com.project.wallet.domain.Wallet;
import com.project.wallet.repository.WalletRepository;
import com.project.wallet.vo.WalletBalanceResponseVO;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    private final HistorialBalanceService historicalBalanceService;

    @Transactional
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

        historicalBalanceService.create(newWallet);

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

    @Transactional
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

        historicalBalanceService.create(updatedWallet);

        WalletResponseVO walletResponse = WalletResponseVO.from(updatedWallet);

        log.info("Deposit successful. Updated wallet: {}", walletResponse);

        return walletResponse;
    }

    @Transactional
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

        historicalBalanceService.create(updatedWallet);

        WalletResponseVO walletResponse = WalletResponseVO.from(updatedWallet);

        log.info("Withdrawal successful. Updated wallet: {}", walletResponse);

        return walletResponse;
    }

    @Transactional
    public WalletResponseVO transfer(Long id, BigDecimal amount, Long recipientId){
        log.info("Transferring {} from wallet with id: {} to wallet with id: {}", amount, id, recipientId);

        Wallet senderWallet;
        Wallet recipientWallet;

        try {
            senderWallet = walletRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Sender wallet not found with id: " + id));
            recipientWallet = walletRepository.findById(recipientId)
                    .orElseThrow(() -> new NoSuchElementException("Recipient wallet not found with id: " + recipientId));
        } catch(NoSuchElementException e) {
            log.error(e.getMessage());
            throw e;
        }

        senderWallet.transferFunds(amount, recipientWallet);

        walletRepository.save(senderWallet);

        historicalBalanceService.create(senderWallet);

        walletRepository.save(recipientWallet);

        historicalBalanceService.create(recipientWallet);

        WalletResponseVO senderWalletResponse = WalletResponseVO.from(senderWallet);

        log.info("Transfer successful. Sender wallet: {}", senderWalletResponse);

        return senderWalletResponse;
    }

    public WalletBalanceResponseVO retrieveHistoricalBalance(Long id, LocalDateTime date) {
        log.info("Retrieving historical balance for wallet with id: {} on date: {}", id, date);

        Wallet wallet;
        try {
            wallet = walletRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Wallet not found with id: " + id));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw e;
        }

        HistoricalBalance historicalBalance = historicalBalanceService.findLastByWalletIdAndTransactionDateLessThanEqual(wallet.getId(), date);

        WalletBalanceResponseVO walletBalanceResponseVO = WalletBalanceResponseVO.from(historicalBalance);

        log.info("Retrieved historical balance: {} for wallet with id: {}", walletBalanceResponseVO.getBalance(), wallet.getId());

        return walletBalanceResponseVO;
    }
}
