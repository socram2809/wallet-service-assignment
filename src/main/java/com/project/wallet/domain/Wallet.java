package com.project.wallet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Slf4j
@ToString
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String userIdentification;

    @NotNull
    private BigDecimal balance;

    public Wallet(String userIdentification) {
        this.userIdentification = userIdentification;
        this.balance = BigDecimal.ZERO;
    }

    public void depositFunds(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        } else {
            String error = "Deposit amount must be positive";
            log.error(error);
            throw new IllegalArgumentException(error);
        }
    }

    public void withdrawFunds(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            String error = "Withdrawal amount must be positive and less than or equal to the balance";
            log.error(error);
            throw new IllegalArgumentException("Withdrawal amount must be positive and less than or equal to the balance");
        }
    }

    public void transferFunds(BigDecimal amount, Wallet recipient) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            recipient.depositFunds(amount);
        } else {
            String error = "Transfer amount must be positive and less than or equal to the balance";
            log.error(error);
            throw new IllegalArgumentException("Transfer amount must be positive and less than or equal to the balance");
        }
    }
}
