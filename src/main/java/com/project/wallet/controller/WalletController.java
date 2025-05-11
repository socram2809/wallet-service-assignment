package com.project.wallet.controller;

import com.project.wallet.service.WalletService;
import com.project.wallet.vo.WalletBalanceResponseVO;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @ApiResponse(responseCode = "201", description = "Wallet created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WalletResponseVO create(
            @Valid @RequestBody WalletCreateRequestVO walletCreateRequest) {
        return walletService.create(walletCreateRequest);
    }

    @Operation(
            description = "Retrieve balance from wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Balance retrieved from wallet")
    @GetMapping("{id}/balance")
    public WalletBalanceResponseVO retrieveBalance(@PathVariable Long id) {
        return walletService.retrieveBalance(id);
    }

    @Operation(
            description = "Retrieve historical balance from wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Historical balance retrieved from wallet")
    @GetMapping("{id}/date/{date}/balance")
    public WalletBalanceResponseVO retrieveHistoricalBalance(@PathVariable Long id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return walletService.retrieveHistoricalBalance(id, date);
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

    @Operation(
            description = "Transfer funds from wallet",
            tags = {"Wallet Management"})
    @ApiResponse(responseCode = "200", description = "Funds transferred from the wallet")
    @PostMapping("{id}/transfer/{amount}/recipient/{recipientId}")
    public WalletResponseVO transfer(@PathVariable Long id, @PathVariable BigDecimal amount, @PathVariable Long recipientId){
        return walletService.transfer(id, amount, recipientId);
    }
}
