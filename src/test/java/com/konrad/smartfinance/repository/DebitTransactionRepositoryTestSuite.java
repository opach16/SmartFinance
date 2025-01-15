package com.konrad.smartfinance.repository;

import com.konrad.smartfinance.domain.DebitTransactionType;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import com.konrad.smartfinance.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class DebitTransactionRepositoryTestSuite {

    private User user;
    private DebitTransaction transaction;

    @Autowired
    private DebitTransactionRepository debitTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user = new User("testUsername", "testEmail", "testPassword");
        transaction = new DebitTransaction(user, DebitTransactionType.EXPENSE, "testName",
                BigDecimal.ONE, BigDecimal.TWO, LocalDate.of(2025, 1, 1));
        userRepository.save(user);
        debitTransactionRepository.save(transaction);
    }

    @Test
    public void findAll() {
        //given
        //when
        List<DebitTransaction> fetchedTransactions = debitTransactionRepository.findAll();
        //then
        assertEquals(1, fetchedTransactions.size());
    }

    @Test
    public void findAllByUser() {
        //given
        //when
        List<DebitTransaction> fetchedTransactions = debitTransactionRepository.findByUser(user);
        //then
        assertEquals(1, fetchedTransactions.size());
        assertEquals(transaction.getUser(), user);
    }
}
