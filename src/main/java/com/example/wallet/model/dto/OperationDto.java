package com.example.wallet.model.dto;

import com.example.wallet.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal operationAmount;
}
