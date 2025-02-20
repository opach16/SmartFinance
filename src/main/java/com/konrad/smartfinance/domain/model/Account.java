package com.konrad.smartfinance.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIN_CURRENCY_ID")
    private Currency mainCurrency;

    @Column(name = "MAIN_BALANCE")
    private BigDecimal mainBalance = BigDecimal.ZERO;

    @Column(name = "ASSETS_BALANCE")
    private BigDecimal assetsBalance = BigDecimal.ZERO;

    @NotNull
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public Account(User user, Currency mainCurrency, BigDecimal mainBalance) {
        this.user = user;
        this.mainCurrency = mainCurrency;
        this.mainBalance = mainBalance;
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
