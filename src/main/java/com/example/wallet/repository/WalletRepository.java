package com.example.wallet.repository;

import com.example.wallet.model.entity.WalletEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "jdbc-mode", havingValue = "blocking")
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    @Query(value="SELECT amount from wallets WHERE wallet_id = uuid(:id)", nativeQuery = true)
    BigDecimal findWalletAmount(@Param("id") String wallet_id);
}
