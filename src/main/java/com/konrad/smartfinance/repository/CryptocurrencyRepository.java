package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Cryptocurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends CrudRepository<Cryptocurrency, Long> {

    @Override
    List<Cryptocurrency> findAll();

    Optional<Cryptocurrency> findBySymbol(String symbol);
}
