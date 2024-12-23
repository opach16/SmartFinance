package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Override
    List<Currency> findAll();
}
