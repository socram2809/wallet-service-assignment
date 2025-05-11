package com.project.wallet.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletResponseVO {

    private Long id;
    private String userIdentification;
    private BigDecimal balance;
}
