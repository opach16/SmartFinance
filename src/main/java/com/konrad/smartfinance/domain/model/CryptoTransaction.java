package com.konrad.smartfinance.domain.model;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "CRYPTO_TRANSACTIONS")
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRYPTO_ID")
    private Cryptocurrency cryptocurrency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE")
    private CryptoTransactionType cryptoTransactionType;

    @NotNull
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;

    @NotNull
    @Column(name = "TRANSACTION_DATE")
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public CryptoTransaction(User user, Cryptocurrency cryptocurrency, CryptoTransactionType cryptoTransactionType, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.cryptocurrency = cryptocurrency;
        this.cryptoTransactionType = cryptoTransactionType;
        this.amount = amount;
        this.price = price;
        this.transactionDate = transactionDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
