package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyTransactionRepository extends CrudRepository<CurrencyTransaction, Long> {

    @Override
    List<CurrencyTransaction> findAll();

    List<CurrencyTransaction> findByUserId(Long userId);
}
