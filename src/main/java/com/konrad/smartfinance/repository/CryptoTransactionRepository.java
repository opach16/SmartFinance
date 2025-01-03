package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.CryptoTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoTransactionRepository extends CrudRepository<CryptoTransaction, Long> {

    @Override
    List<CryptoTransaction> findAll();

    List<CryptoTransaction> findByUserId(Long userId);
}
