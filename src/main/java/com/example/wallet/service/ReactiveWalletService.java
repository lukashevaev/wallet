package com.example.wallet.service;

import com.example.wallet.model.dto.OperationContext;
import com.example.wallet.model.dto.OperationDto;
import com.example.wallet.model.enums.OperationErrorType;
import com.example.wallet.repository.ReactiveWalletDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class ReactiveWalletService {

    private final ReactiveWalletDao reactiveWalletDao;
    private final ObjectMapper objectMapper;

    public Mono<BigDecimal> reactiveFindWalletAmount(String walletId) {
        return reactiveWalletDao.findWalletAmount(walletId);
    }

    public Mono<OperationContext> reactiveUpdateWallet(String operationDto) {
        return Mono.fromSupplier(() -> deserializeOperationDto(operationDto))
                .map(context -> {
                    if (context.getErrorType() != null) {
                        return context;
                    }

                    if (context.getDto().getWalletId() == null
                            || context.getDto().getOperationAmount() == null
                            || context.getDto().getOperationType() == null) {
                        context.setErrorType(OperationErrorType.MISSED_FIELDS);
                        return context;
                    }

                    if (context.getDto().getOperationAmount().compareTo(BigDecimal.ZERO) < 0) {
                        context.setErrorType(OperationErrorType.NEGATIVE_AMOUNT);
                        return context;
                    }

                    return context;
                })
                .flatMap(context -> {
                    if (context.getErrorType() != null) {
                        return Mono.just(context);
                    }

                    return reactiveWalletDao.updateWallet(context.getDto())
                            .map(updatedCount -> OperationContext.builder()
                                    .errorType(updatedCount > 0 ? null : OperationErrorType.WALLET_NOT_EXIST)
                                    .build())
                            .onErrorResume(Exception.class, e -> {
                                if (e.getMessage().contains("violates check constraint")) {
                                    return Mono.just(OperationContext.builder()
                                            .errorType(OperationErrorType.CONSTRAINT_VIOLATION)
                                            .build());
                                } else {
                                    return Mono.just(OperationContext.builder()
                                            .errorType(OperationErrorType.UNKNOWN_ERROR)
                                            .build());
                                }
                            });
                });
    }

    private OperationContext deserializeOperationDto(String operationDto) {

        try {
            return OperationContext.builder()
                    .dto(objectMapper.readValue(operationDto, OperationDto.class))
                    .build();
        } catch (JsonProcessingException e) {
            return OperationContext.builder()
                    .errorType(OperationErrorType.INVALID_JSON)
                    .build();
        }
    }
}
