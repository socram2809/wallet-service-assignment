package com.project.wallet.service;

import com.project.wallet.domain.Wallet;
import com.project.wallet.repository.WalletRepository;
import com.project.wallet.vo.WalletBalanceResponseVO;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private HistoricalBalanceService historicalBalanceService;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService(walletRepository, historicalBalanceService);
    }

    @Test
    void testCreate() {
        String userIdentification = "user123";
        WalletCreateRequestVO walletCreateRequest = new WalletCreateRequestVO();
        walletCreateRequest.setUserIdentification(userIdentification);
        Wallet wallet = new Wallet(userIdentification);

        when(walletRepository.save(eq(wallet))).thenReturn(wallet);

        WalletResponseVO walletResponse = walletService.create(walletCreateRequest);

        assertNotNull(walletResponse);
        assertEquals(userIdentification, walletResponse.getUserIdentification());
        assertEquals(BigDecimal.ZERO, walletResponse.getBalance());
    }

    @Test
    void testRetrieveBalance() {
        Long walletId = 1L;
        Wallet wallet = new Wallet("user123");
        wallet.depositFunds(new BigDecimal("100.00"));

        when(walletRepository.findById(eq(walletId)))
                .thenReturn(Optional.of(wallet));

        WalletBalanceResponseVO walletResponse = walletService.retrieveBalance(walletId);

        assertNotNull(walletResponse);
        assertEquals(wallet.getBalance(), walletResponse.getBalance());
    }

    @Test
    void testDeposit() {
        Long walletId = 1L;
        String userIdentification = "user123";
        BigDecimal depositAmount = new BigDecimal("50.00");
        Wallet wallet = new Wallet(userIdentification);
        Wallet walletWithDepositedAmount = new Wallet(userIdentification);
        walletWithDepositedAmount.depositFunds(depositAmount);

        when(walletRepository.findById(eq(walletId))).thenReturn(Optional.of(wallet));
        when(walletRepository.save(eq(walletWithDepositedAmount))).thenReturn(walletWithDepositedAmount);

        WalletResponseVO walletResponse = walletService.deposit(walletId, depositAmount);

        assertNotNull(walletResponse);
        assertEquals(walletWithDepositedAmount.getBalance(), walletResponse.getBalance());
        assertEquals(walletWithDepositedAmount.getUserIdentification(), walletResponse.getUserIdentification());
    }

    @Test
    void testWithdraw() {
        Long walletId = 1L;
        String userIdentification = "user123";
        BigDecimal withdrawAmount = new BigDecimal("30.00");
        BigDecimal funds = new BigDecimal("100.00");
        Wallet wallet = new Wallet("user123");
        wallet.depositFunds(funds);
        Wallet walletWithWithdrawnAmount = new Wallet(userIdentification);
        walletWithWithdrawnAmount.depositFunds(funds);
        walletWithWithdrawnAmount.withdrawFunds(withdrawAmount);

        when(walletRepository.findById(eq(walletId)))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.save(eq(walletWithWithdrawnAmount))).thenReturn(walletWithWithdrawnAmount);

        WalletResponseVO walletResponse = walletService.withdraw(walletId, withdrawAmount);

        assertNotNull(walletResponse);
        assertEquals(walletWithWithdrawnAmount.getBalance(), walletResponse.getBalance());
        assertEquals(walletWithWithdrawnAmount.getUserIdentification(), walletResponse.getUserIdentification());
    }

    @Test
    void testTransfer() {
        Long senderId = 1L;
        Long recipientId = 2L;
        String sender = "user123";
        String recipient = "user456";
        BigDecimal funds = new BigDecimal("100.00");
        BigDecimal transferAmount = new BigDecimal("50.00");
        Wallet senderWallet = new Wallet(sender);
        senderWallet.depositFunds(funds);
        Wallet recipientWallet = new Wallet(recipient);
        Wallet senderWalletWithUpdatedFunds = new Wallet(sender);
        senderWalletWithUpdatedFunds.depositFunds(funds);
        Wallet recipientWalletWithUpdatedFunds = new Wallet(recipient);
        senderWalletWithUpdatedFunds.transferFunds(transferAmount, recipientWalletWithUpdatedFunds);

        when(walletRepository.findById(eq(senderId)))
                .thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(eq(recipientId)))
                .thenReturn(Optional.of(recipientWallet));
        when(walletRepository.save(eq(senderWalletWithUpdatedFunds))).thenReturn(senderWalletWithUpdatedFunds);
        when(walletRepository.save(eq(recipientWalletWithUpdatedFunds))).thenReturn(recipientWalletWithUpdatedFunds);

        WalletResponseVO walletResponse = walletService.transfer(senderId, transferAmount, recipientId);

        assertNotNull(walletResponse);
        assertEquals(senderWallet.getBalance(), walletResponse.getBalance());
        assertEquals(senderWallet.getUserIdentification(), walletResponse.getUserIdentification());
        assertEquals(senderWallet.getId(), walletResponse.getId());
    }
}
