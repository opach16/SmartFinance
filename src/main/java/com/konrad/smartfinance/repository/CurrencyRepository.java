package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Currency;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Override
    List<Currency> findAll();

    Optional<Currency> findBySymbol(@NotNull String symbol);
}
