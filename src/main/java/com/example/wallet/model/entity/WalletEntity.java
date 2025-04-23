package com.example.wallet.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name="wallets")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletEntity {
        @Id
        @GeneratedValue
        @UuidGenerator
        @Column(name = "wallet_id")
        private UUID walletId;

        @Column(name = "amount")
        private Long amount;
}
