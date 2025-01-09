package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetsService {

    private final AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset addAsset(Asset asset) {
        Asset fetchedAsset;
        if (assetRepository.findByName(asset.getName()).isPresent()) {
            fetchedAsset = assetRepository.findByName(asset.getName()).get();
            fetchedAsset.setAmount(fetchedAsset.getAmount().add(asset.getAmount()));
            assetRepository.save(fetchedAsset);
        } else {
            fetchedAsset = assetRepository.save(asset);
        }
        return fetchedAsset;
    }

    public void deleteAsset(Asset asset) {
        Asset fetchedAsset = assetRepository.findByName(asset.getName()).get();
        fetchedAsset.setAmount(fetchedAsset.getAmount().subtract(asset.getAmount()));
        assetRepository.save(fetchedAsset);
        if (fetchedAsset.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            assetRepository.delete(fetchedAsset);
        }
    }
}
