package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.dto.AssetDto;
import com.konrad.smartfinance.domain.model.*;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class AssetsMapperTestSuite {

    private Cryptocurrency crypto;
    private Currency currency;
    private Currency mainCurrency;
    private Account account;
    private User user;

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @BeforeEach
    void setUp() {
        crypto = Cryptocurrency.builder()
                .symbol("testSymbol1")
                .name("testName1")
                .price(new BigDecimal("40"))
                .build();
        cryptocurrencyRepository.save(crypto);
        currency = Currency.builder()
                .symbol("USD")
                .price(new BigDecimal("5"))
                .build();
        currencyRepository.save(currency);
        mainCurrency = Currency.builder()
                .id(2L)
                .symbol("USD")
                .price(new BigDecimal("5"))
                .build();
        account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(mainCurrency)
                .mainBalance(new BigDecimal("100"))
                .build();
        user = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        user.setAccount(account);
    }

    @Test
    void shouldMapToAssetDtoCrypto() {
        //given
        Asset asset = Asset.builder()
                .id(1L)
                .user(user)
                .assetType(AssetType.CRYPTOCURRENCY)
                .name(crypto.getSymbol())
                .amount(new BigDecimal("15"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        AssetDto assetDto = assetsMapper.mapToAssetDto(asset);
        //then
        assertNotNull(assetDto);
        assertEquals(asset.getId(), assetDto.getId());
        assertEquals(asset.getUser().getId(), assetDto.getUser().getId());
        assertEquals(asset.getAssetType(), assetDto.getAssetType());
        assertEquals(asset.getName(), assetDto.getName());
        assertEquals(asset.getAmount(), assetDto.getAmount());
        assertEquals(asset.getCreatedAt(), assetDto.getCreatedAt());
        assertEquals(asset.getUpdatedAt(), assetDto.getUpdatedAt());
        assertEquals(asset.getAmount().multiply(crypto.getPrice()).divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), assetDto.getCurrentValue());

    }

    @Test
    void shouldMapToAssetDtoCurrency() {
        //given
        Asset asset = Asset.builder()
                .id(1L)
                .user(user)
                .assetType(AssetType.CURRENCY)
                .name(currency.getSymbol())
                .amount(new BigDecimal("5"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        AssetDto assetDto = assetsMapper.mapToAssetDto(asset);
        //then
        assertNotNull(assetDto);
        assertEquals(asset.getId(), assetDto.getId());
        assertEquals(asset.getUser().getId(), assetDto.getUser().getId());
        assertEquals(asset.getAssetType(), assetDto.getAssetType());
        assertEquals(asset.getName(), assetDto.getName());
        assertEquals(asset.getAmount(), assetDto.getAmount());
        assertEquals(asset.getCreatedAt(), assetDto.getCreatedAt());
        assertEquals(asset.getUpdatedAt(), assetDto.getUpdatedAt());
        assertEquals(asset.getAmount().multiply(currency.getPrice()).divide(mainCurrency.getPrice(), 2, RoundingMode.CEILING), assetDto.getCurrentValue());
    }

    @Test
    void mapToAssetDtoList() {
        //given
        Asset asset1 = Asset.builder()
                .id(1L)
                .user(user)
                .assetType(AssetType.CRYPTOCURRENCY)
                .name(crypto.getSymbol())
                .amount(new BigDecimal("15"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        Asset asset2 = Asset.builder()
                .id(2L)
                .user(user)
                .assetType(AssetType.CURRENCY)
                .name(currency.getSymbol())
                .amount(new BigDecimal("5"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        List<Asset> assetList = List.of(asset1, asset2);
        //when
        List<AssetDto> assetDtoList = assetsMapper.mapToAssetDtoList(assetList);
        //then
        assertNotNull(assetDtoList);
        assertEquals(assetList.size(), assetDtoList.size());
        assertEquals(asset1.getId(), assetDtoList.getFirst().getId());
        assertEquals(asset1.getAssetType(), assetDtoList.getFirst().getAssetType());
        assertEquals(asset2.getId(), assetDtoList.getLast().getId());
        assertEquals(asset2.getAssetType(), assetDtoList.getLast().getAssetType());

    }
}