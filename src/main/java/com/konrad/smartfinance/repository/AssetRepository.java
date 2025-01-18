package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.AssetException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {

    @Override
    List<Asset> findAll();

    Optional<Asset> findByName(String name);

    List<Asset> findByUser(User user);

    default Optional<Asset> findByUserAndName(User user, String name) {
        List<Asset> assetList = findByUser(user);
        return assetList.stream().filter(asset -> asset.getName().equals(name)).findFirst();
    }
}
