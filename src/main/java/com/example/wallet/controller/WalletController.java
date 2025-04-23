package com.example.wallet.controller;

import com.example.wallet.model.dto.OperationContext;
import com.example.wallet.service.BlockingWalletService;
import com.example.wallet.service.ReactiveWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WalletController {

    private final ReactiveWalletService walletService;

    @GetMapping("/wallets")
    public Mono<OperationContext> findWalletAmount(@RequestParam(name = "wallet_id") String walletId) { // TODO change repo to service
        return walletService.reactiveFindWalletAmount(walletId)
                .map(amount -> OperationContext.builder().value(amount).build());
    }

    @PostMapping("/wallet")
    public Mono<OperationContext> updateWalletAmount(@RequestBody String operationDto) {

        return walletService.reactiveUpdateWallet(operationDto);
    }
}
