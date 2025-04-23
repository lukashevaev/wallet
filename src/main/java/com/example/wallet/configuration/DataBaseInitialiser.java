package com.example.wallet.configuration;

import com.example.wallet.model.entity.WalletEntity;
import com.example.wallet.repository.WalletRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnBean(WalletRepository.class)
public class DataBaseInitialiser {

    private final WalletRepository walletRepository;

    //@PostConstruct
    public void init() {

        for (int i = 0; i < 10; i++) {
            var walletEntity = new WalletEntity();
            walletEntity.setAmount(new Random().nextLong(2000000));
            try {
                walletRepository.save(walletEntity);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
