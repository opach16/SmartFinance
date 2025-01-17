package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AccountMapperTestSuite {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    void mapToAccountDto() {
        //given
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .password("testPassword")
                .email("test@email.com")
                .build();
        Currency currency = Currency.builder()
                .id(1L)
                .symbol("testSymbol")
                .price(BigDecimal.ONE)
                .build();
        Account account = Account.builder()
                .id(1L)
                .user(user)
                .mainCurrency(currency)
                .mainBalance(new BigDecimal("100"))
                .assetsBalance(new BigDecimal("50"))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        AccountDto mappedAccount = accountMapper.mapToAccountDto(account);
        //then
        assertNotNull(mappedAccount);
        assertEquals(account.getId(), mappedAccount.getId());
        assertEquals(user.getId(), mappedAccount.getUser().getId());
        assertEquals(user.getUsername(), mappedAccount.getUser().getUsername());
        assertEquals(currency.getId(), mappedAccount.getMainCurrency().getId());
        assertEquals(currency.getSymbol(), mappedAccount.getMainCurrency().getSymbol());
        assertEquals(account.getMainBalance(), mappedAccount.getMainBalance());
        assertEquals(account.getAssetsBalance(), mappedAccount.getAssetsBalance());
        assertEquals(new BigDecimal("150"), mappedAccount.getTotalBalance());
        assertEquals(account.getCreatedAt(), mappedAccount.getCreatedAt());
        assertEquals(account.getUpdatedAt(), mappedAccount.getUpdatedAt());
    }
}