package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AssetDto;
import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetsMapper {

    private final UserMapper userMapper;
    private final CurrencyRepository currencyRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public Asset mapToAsset(AssetDto assetDto) {
        return Asset.builder()
                .user(userMapper.mapToUserEntity(assetDto.getUser()))
                .assetType(assetDto.getAssetType())
                .name(assetDto.getName())
                .amount(assetDto.getAmount())
                .build();
    }

    public AssetDto mapToAssetDto(Asset asset) {
        return AssetDto.builder()
                .id(asset.getId())
                .user(userMapper.mapToUserDto(asset.getUser()))
                .assetType(asset.getAssetType())
                .name(asset.getName())
                .amount(asset.getAmount())
                .currentValue(calculateCurrentValue(asset))
                .build();
    }

    public List<Asset> mapToAssetList(List<AssetDto> assetDtoList) {
        return assetDtoList.stream()
                .map(this::mapToAsset)
                .toList();
    }

    public List<AssetDto> mapToAssetDtoList(List<Asset> assetList) {
        return assetList.stream()
                .map(this::mapToAssetDto)
                .toList();
    }

    private BigDecimal calculateCurrentValue(Asset asset) {
        return switch (asset.getAssetType()) {
            case CRYPTOCURRENCY -> cryptocurrencyRepository.findBySymbol(asset.getName())
                    .map(cryptocurrency -> asset.getAmount().multiply(cryptocurrency.getPrice()))
                    .orElse(BigDecimal.ZERO);
            case CURRENCY -> currencyRepository.findBySymbol(asset.getName())
                    .map(currency -> asset.getAmount().multiply(currency.getPrice()))
                    .orElse(BigDecimal.ZERO);
        };
    }
}
