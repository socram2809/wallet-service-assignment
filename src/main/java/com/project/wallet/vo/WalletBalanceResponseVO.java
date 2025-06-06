package com.project.wallet.vo;

import com.project.wallet.domain.HistoricalBalance;
import com.project.wallet.domain.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletBalanceResponseVO {

    private BigDecimal balance;

    public static WalletBalanceResponseVO from(Wallet wallet) {
        WalletBalanceResponseVO response = new WalletBalanceResponseVO();
        response.setBalance(wallet.getBalance());
        return response;
    }

    public static WalletBalanceResponseVO from(HistoricalBalance historicalBalance) {
        WalletBalanceResponseVO response = new WalletBalanceResponseVO();
        response.setBalance(historicalBalance.getBalance());
        return response;
    }
}
