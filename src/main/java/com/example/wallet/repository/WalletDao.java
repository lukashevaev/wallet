package com.example.wallet.repository;

import com.example.wallet.model.dto.OperationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.wallet.model.enums.OperationType.DEPOSIT;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WalletDao {

    private static final String UPDATE = """
            UPDATE wallets
            SET amount = amount + ?
            WHERE wallet_id = ?
            """;

    public int updatedWallet(OperationDto operationDto) {
        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/postgres",
                        "admin", "admin");
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setBigDecimal(1, DEPOSIT.equals(operationDto.getOperationType()) ? operationDto.getOperationAmount() : operationDto.getOperationAmount().negate());
            preparedStatement.setObject(2, operationDto.getWalletId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
