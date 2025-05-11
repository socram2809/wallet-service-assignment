package com.project.wallet.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Slf4j
public class HistoricalBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Wallet wallet;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private LocalDateTime transactionDate;

    public HistoricalBalance(Wallet wallet) {
        this.wallet = wallet;
        this.balance = wallet.getBalance();
        this.transactionDate = LocalDateTime.now();
    }
}
