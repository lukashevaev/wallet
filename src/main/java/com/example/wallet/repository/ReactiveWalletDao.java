package com.example.wallet.repository;

import com.example.wallet.model.dto.OperationDto;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.example.wallet.model.enums.OperationType.DEPOSIT;

@AllArgsConstructor
@Service
@ConditionalOnProperty(name = "jdbc-mode", havingValue = "reactive")
public class ReactiveWalletDao {

    private static final String UPDATE = """
            with updated as (UPDATE wallets
                                       SET amount = amount + :amount
                                       WHERE wallet_id = uuid(:id)
                                       RETURNING *)
                       SELECT COUNT(*) as cnt FROM updated
            """;

    private final ConnectionFactory connectionFactory;

    private DatabaseClient databaseClient;

    @PostConstruct
    public void init() {
        databaseClient = DatabaseClient.create(connectionFactory);
    }

    public Mono<BigDecimal> findWalletAmount(String wallet) {
        return databaseClient.sql("SELECT amount from wallets WHERE wallet_id = uuid(:id)")
                .bind("id", wallet)
                .map(row -> (BigDecimal) row.get(0))
                .first();
    }

    public Mono<Long> updateWallet(OperationDto dto) {
        return databaseClient.sql(UPDATE)
                .bind("amount", DEPOSIT.equals(dto.getOperationType()) ? dto.getOperationAmount() : dto.getOperationAmount().negate())
                .bind("id", dto.getWalletId())
                .map(row -> (Long) row.get("cnt"))
                .first();
    }
}
