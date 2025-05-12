package com.project.wallet.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    @Test
    void testDepositFunds() {
        Wallet wallet = new Wallet("user123");
        wallet.depositFunds(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("50.00"), wallet.getBalance());
    }

    @Test
    void testDepositNegativeFunds() {
        Wallet wallet = new Wallet("user123");
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class, () -> wallet.depositFunds(new BigDecimal("-50.00")));
        assertThat(ex.getMessage()).contains("Deposit amount must be positive");
    }

    @Test
    void testWithdrawFunds() {
        Wallet wallet = new Wallet("user123");
        wallet.depositFunds(new BigDecimal("50.00"));
        wallet.withdrawFunds(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("20.00"), wallet.getBalance());
    }

    @Test
    void testWithdrawMoreThanBalance() {
        Wallet wallet = new Wallet("user123");
        wallet.depositFunds(new BigDecimal("50.00"));
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class, () -> wallet.withdrawFunds(new BigDecimal("60.00")));
        assertThat(ex.getMessage()).contains("Withdrawal amount must be positive and less than or equal to the balance");
    }

    @Test
    void testWithdrawNegativeFunds() {
        Wallet wallet = new Wallet("user123");
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class, () -> wallet.withdrawFunds(new BigDecimal("-30.00")));
        assertThat(ex.getMessage()).contains("Withdrawal amount must be positive and less than or equal to the balance");
    }

    @Test
    void testTransferFunds() {
        Wallet sender = new Wallet("user123");
        sender.depositFunds(new BigDecimal("100.00"));
        Wallet recipient = new Wallet("user456");
        sender.transferFunds(new BigDecimal("50.00"), recipient);
        assertEquals(new BigDecimal("50.00"), sender.getBalance());
        assertEquals(new BigDecimal("50.00"), recipient.getBalance());
    }

    @Test
    void testTransferMoreThanBalance() {
        Wallet sender = new Wallet("user123");
        sender.depositFunds(new BigDecimal("100.00"));
        Wallet recipient = new Wallet("user456");
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class, () -> sender.transferFunds(new BigDecimal("150.00"), recipient));
        assertThat(ex.getMessage()).contains("Transfer amount must be positive and less than or equal to the balance");
    }

    @Test
    void testTransferNegativeFunds() {
        Wallet sender = new Wallet("user123");
        Wallet recipient = new Wallet("user456");
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class, () -> sender.transferFunds(new BigDecimal("-50.00"), recipient));
        assertThat(ex.getMessage()).contains("Transfer amount must be positive and less than or equal to the balance");
    }
}
