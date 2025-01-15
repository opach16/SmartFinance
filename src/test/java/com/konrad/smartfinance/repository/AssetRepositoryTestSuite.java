package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.AssetType;
import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AssetRepositoryTestSuite {

    private User user;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user = new User("testUsername", "testEmail", "testPassword");
        userRepository.save(user);
    }

    @Test
    void findAll() {
        //given
        Asset asset = new Asset(user, AssetType.CURRENCY, "testCurrency", BigDecimal.ONE);
        assetRepository.save(asset);
        //when
        List<Asset> fetchedAssets = assetRepository.findAll();
        //then
        assertEquals(1, fetchedAssets.size());
    }

    @Test
    void findByName() {
        //given
        Asset asset = new Asset(user, AssetType.CURRENCY, "testCurrency", BigDecimal.ONE);
        assetRepository.save(asset);
        //when
        Optional<Asset> fetchedAsset = assetRepository.findByName(asset.getName());
        //then
        assertTrue(fetchedAsset.isPresent());
        assertEquals(asset.getName(), fetchedAsset.get().getName());
    }
}