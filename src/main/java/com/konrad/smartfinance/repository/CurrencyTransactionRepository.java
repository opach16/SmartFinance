package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyTransactionRepository extends CrudRepository<CurrencyTransaction, Long> {
}
