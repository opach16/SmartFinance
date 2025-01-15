package com.konrad.smartfinance.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DebitTransactionRepositoryTestSuite {

    @Test
    public void findAll() {}
//    @Autowired
//    private AccountTransactionRepository accountTransactionRepository;
//
//    @Test
//    public void findAll() {
//        //given
//        User user = new User("testUsername", "testEmail", "testPassword");
//        AccountTransaction accountTransaction = new AccountTransaction(user, AccountTransactionType.EXPENSE, "testName", BigDecimal.ONE, BigDecimal.TWO, LocalDate.of(2025, 1, 1));
//        accountTransactionRepository.save(accountTransaction);
//        //when
//        List<AccountTransaction> accountTransactions = accountTransactionRepository.findAll();
//        //then
//        assertEquals(1, accountTransactions.size());
//    }
}
