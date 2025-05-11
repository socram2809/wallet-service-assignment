package com.project.wallet.vo;

import com.project.wallet.domain.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletResponseVO {

    private Long id;
    private String userIdentification;
    private BigDecimal balance;

    public static WalletResponseVO from(Wallet wallet) {
        WalletResponseVO response = new WalletResponseVO();
        response.setId(wallet.getId());
        response.setUserIdentification(wallet.getUserIdentification());
        response.setBalance(wallet.getBalance());
        return response;
    }
}
