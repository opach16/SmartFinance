package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.model.AccountTransaction;
import com.konrad.smartfinance.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {

    @Override
    List<AccountTransaction> findAll();
    List<AccountTransaction> findByUser(User user);
}
