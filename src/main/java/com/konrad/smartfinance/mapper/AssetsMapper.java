package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AssetDto;
import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetsMapper {

    private final UserMapper userMapper;
    private final CurrencyRepository currencyRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public AssetDto mapToAssetDto(Asset asset) {
        return AssetDto.builder()
                .id(asset.getId())
                .user(userMapper.mapToUserDto(asset.getUser()))
                .assetType(asset.getAssetType())
                .name(asset.getName())
                .amount(asset.getAmount())
                .currentValue(calculateCurrentValue(asset))
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }

    public List<AssetDto> mapToAssetDtoList(List<Asset> assetList) {
        return assetList.stream()
                .map(this::mapToAssetDto)
                .toList();
    }

    private BigDecimal calculateCurrentValue(Asset asset) {
        BigDecimal mainCurrencyPrice = asset.getUser().getAccount().getMainCurrency().getPrice();
        return switch (asset.getAssetType()) {
            case CRYPTOCURRENCY -> cryptocurrencyRepository.findBySymbol(asset.getName())
                    .map(cryptocurrency -> calculateValue(asset.getAmount(), cryptocurrency.getPrice(), mainCurrencyPrice))
                    .orElse(BigDecimal.ZERO);
            case CURRENCY -> currencyRepository.findBySymbol(asset.getName())
                    .map(currency -> calculateValue(asset.getAmount(), currency.getPrice(), mainCurrencyPrice))
                    .orElse(BigDecimal.ZERO);
        };
    }

    private BigDecimal calculateValue(BigDecimal amount, BigDecimal assetPrice, BigDecimal mainUserCurrencyPrice) {
        return amount.multiply(assetPrice).divide(mainUserCurrencyPrice, 2, RoundingMode.CEILING);
    }
}
