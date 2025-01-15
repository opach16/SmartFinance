package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebitTransactionRepository extends CrudRepository<DebitTransaction, Long> {

    @Override
    List<DebitTransaction> findAll();

    List<DebitTransaction> findByUser(User user);
}
