package com.konrad.smartfinance.domain.model;

import com.konrad.smartfinance.domain.AccountTransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
@Table(name = "TRANSACTIONS")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE")
    private AccountTransactionType transactionType;

    @Column(name = "NAME")
    private String name;

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

    public AccountTransaction(User user, AccountTransactionType transactionType, String name, BigDecimal amount, BigDecimal price, LocalDate transactionDate) {
        this.user = user;
        this.transactionType = transactionType;
        this.name = name;
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
