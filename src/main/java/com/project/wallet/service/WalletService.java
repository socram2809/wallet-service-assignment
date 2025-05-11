package com.project.wallet.service;

import com.project.wallet.domain.Wallet;
import com.project.wallet.repository.WalletRepository;
import com.project.wallet.vo.WalletCreateRequestVO;
import com.project.wallet.vo.WalletResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletResponseVO create(WalletCreateRequestVO walletCreateRequest){
        log.info("Creating wallet for user: {}", walletCreateRequest.getUserIdentification());

        Wallet existingWallet = walletRepository.findByUserIdentification(walletCreateRequest.getUserIdentification());

        if(existingWallet != null){
            String error = "Wallet already exists for user: " + walletCreateRequest.getUserIdentification();
            log.error(error);
            throw new IllegalArgumentException(error);
        }

        Wallet wallet = new Wallet(walletCreateRequest.getUserIdentification());

        Wallet newWallet = walletRepository.save(wallet);

        WalletResponseVO walletResponse = new WalletResponseVO();
        walletResponse.setId(newWallet.getId());
        walletResponse.setUserIdentification(newWallet.getUserIdentification());
        walletResponse.setBalance(newWallet.getBalance());

        log.info("Wallet created successfully: {}", walletResponse);

        return walletResponse;
    }
}
