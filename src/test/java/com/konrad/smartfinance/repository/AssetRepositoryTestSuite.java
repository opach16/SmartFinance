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

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void findByUserAndName() {
        //given
        User user2 = new User("testUsername2", "testEmail2", "testPassword2");
        userRepository.save(user2);
        Asset asset1 = new Asset(user, AssetType.CURRENCY, "testCurrency1", BigDecimal.ONE);
        Asset asset2 = new Asset(user, AssetType.CRYPTOCURRENCY, "testCurrency2", BigDecimal.TWO);
        assetRepository.save(asset1);
        assetRepository.save(asset2);
        //when
        Optional<Asset> testCurrency1 = assetRepository.findByUserAndName(user, "testCurrency1");
        Optional<Asset> testCurrency2 = assetRepository.findByUserAndName(user, "testCurrency2");
        Optional<Asset> testCurrency3 = assetRepository.findByUserAndName(user2, "testCurrency1");
        Optional<Asset> testCurrency4 = assetRepository.findByUserAndName(user, "testCurrency3");
        //then
        assertTrue(testCurrency1.isPresent());
        assertTrue(testCurrency2.isPresent());
        assertFalse(testCurrency3.isPresent());
        assertFalse(testCurrency4.isPresent());
    }
}