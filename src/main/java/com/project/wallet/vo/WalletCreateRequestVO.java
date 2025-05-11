package com.project.wallet.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletCreateRequestVO {

    @NotBlank
    private String userIdentification;
}
