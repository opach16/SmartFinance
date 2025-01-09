package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {

    @Override
    List<Asset> findAll();

    Optional<Asset> findByName(String name);
}
