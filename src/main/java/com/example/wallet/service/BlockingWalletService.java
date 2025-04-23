package com.example.wallet.service;

import com.example.wallet.model.dto.OperationDto;
import com.example.wallet.model.dto.OperationContext;
import com.example.wallet.model.enums.OperationErrorType;
import com.example.wallet.repository.ReactiveWalletDao;
import com.example.wallet.repository.WalletDao;
import com.example.wallet.repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "jdbc-mode", havingValue = "blocking")
public class BlockingWalletService {

    private WalletRepository walletRepository;
    private final WalletDao walletDao;
    private final ObjectMapper objectMapper;

    public BigDecimal findWalletAmount(String walletId) {
        return walletRepository.findWalletAmount(walletId);
    }

    @Transactional
    public OperationContext updateWallet(String operationDto) {
        try {
            var dto = objectMapper.readValue(operationDto, OperationDto.class);
            if (dto.getWalletId() == null
                    || dto.getOperationAmount() == null
                    || dto.getOperationType() == null) {
                return OperationContext.builder()
                        .errorType(OperationErrorType.MISSED_FIELDS)
                        .build();
            }

            if (dto.getOperationAmount().compareTo(BigDecimal.ZERO) < 0) {
                return OperationContext.builder()
                        .errorType(OperationErrorType.NEGATIVE_AMOUNT)
                        .build();
            }

            var updated = walletDao.updatedWallet(dto);
            if (updated == 1) {
                return OperationContext.builder()
                        .build();
            } else {
                return OperationContext.builder()
                        .errorType(OperationErrorType.WALLET_NOT_EXIST)
                        .build();
            }
        } catch (Exception e) {
            if (e instanceof JsonProcessingException) {
                return OperationContext.builder()
                        .errorType(OperationErrorType.INVALID_JSON)
                        .build();
            } else if (e.getMessage().contains("violates check constraint")) {
                return OperationContext.builder()
                        .errorType(OperationErrorType.CONSTRAINT_VIOLATION)
                        .build();
            }
            return OperationContext.builder()
                    .build();
        }
    }
}

