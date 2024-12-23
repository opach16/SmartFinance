package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.CryptoTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoTransactionRepository extends CrudRepository<CryptoTransaction, Long> {
}
