package com.project.wallet.controller;

import com.project.wallet.service.WalletService;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Wallet Management", description = "Wallet management to deposit, withdraw and transfer funds")
@RequestMapping("/wallet")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @Operation(
            description = "Create a new wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Wallet created")
    @PostMapping
    public WalletResponseVO create(
            @Valid @RequestBody WalletCreateRequestVO walletCreateRequest) {
        return walletService.create(walletCreateRequest);
    }

    @Operation(
            description = "Deposit funds on wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Funds deposited on wallet")
    @PostMapping("{id}/deposit/{amount}")
    public WalletResponseVO deposit(@PathVariable Long id, @PathVariable BigDecimal amount){
        return walletService.deposit(id, amount);
    }

    @Operation(
            description = "Withdraw funds on wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Funds withdrawn from the wallet")
    @PostMapping("{id}/withdraw/{amount}")
    public WalletResponseVO withdraw(@PathVariable Long id, @PathVariable BigDecimal amount){
        return walletService.withdraw(id, amount);
    }
}
