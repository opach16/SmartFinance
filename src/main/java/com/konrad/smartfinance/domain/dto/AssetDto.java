package com.konrad.smartfinance.domain.dto;

import com.konrad.smartfinance.domain.AssetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto {

    private Long id;
    private UserDto user;
    private AssetType assetType;
    private String name;
    private BigDecimal amount;
    private BigDecimal currentValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssetDto(UserDto user, AssetType assetType, String name, BigDecimal amount) {
        this.user = user;
        this.assetType = assetType;
        this.name = name;
        this.amount = amount;
    }
}
