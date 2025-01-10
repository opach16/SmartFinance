package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetsService {

    private final AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public void addAsset(Asset asset) {
        assetRepository.findByName(asset.getName()).ifPresentOrElse(presentAsset -> {
            presentAsset.setAmount(presentAsset.getAmount().add(asset.getAmount()));
           assetRepository.save(presentAsset);
           }, () -> assetRepository.save(asset));
    }

    public void deleteAsset(Asset asset) {
        assetRepository.findByName(asset.getName()).ifPresent(presentAsset -> {
            if (presentAsset.getAmount().compareTo(asset.getAmount()) <= 0) {
                assetRepository.delete(presentAsset);
            } else {
                presentAsset.setAmount(presentAsset.getAmount().subtract(asset.getAmount()));
                assetRepository.save(presentAsset);
            }
        });
    }
}
