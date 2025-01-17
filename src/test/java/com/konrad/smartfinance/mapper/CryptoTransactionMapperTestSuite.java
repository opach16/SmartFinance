package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.model.*;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import com.konrad.smartfinance.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CryptoTransactionMapperTestSuite {

    private Currency currency1;
    private Currency currency2;
    private User user1;
    private User user2;
    private Account account1;
    private Account account2;
    private Cryptocurrency crypto1;
    private Cryptocurrency crypto2;

    @Autowired
    private CryptoTransactionMapper cryptoTransactionMapper;

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currency1 = Currency.builder()
                .symbol("USD")
                .price(new BigDecimal("5"))
                .build();
        currency2 = Currency.builder()
                .symbol("EUR")
                .price(new BigDecimal("4"))
                .build();
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);

        user1 = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        user2 = User.builder()
                .id(1L)
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        account1 = Account.builder()
                .id(1L)
                .user(user1)
                .mainCurrency(currency1)
                .mainBalance(new BigDecimal("100"))
                .build();
        account2 = Account.builder()
                .id(2L)
                .user(user1)
                .mainCurrency(currency2)
                .mainBalance(new BigDecimal("400"))
                .build();
        user1.setAccount(account1);
        user2.setAccount(account2);

        crypto1 = Cryptocurrency.builder()
                .symbol("testSymbol1")
                .name("testName1")
                .price(BigDecimal.ONE)
                .build();
        crypto2 = Cryptocurrency.builder()
                .symbol("testSymbol2")
                .name("testName2")
                .price(BigDecimal.TWO)
                .build();
        cryptocurrencyRepository.save(crypto1);
        cryptocurrencyRepository.save(crypto2);
    }

    @Test
    void shouldMapToCryptoTransactionDto() {
        //given
        CryptoTransaction cryptoTransaction = CryptoTransaction.builder()
                .id(1L)
                .user(user1)
                .cryptocurrency(crypto1)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        CryptoTransactionDto cryptoTransactionDto = cryptoTransactionMapper.mapToCryptoTransactionDto(cryptoTransaction);
        //then
        assertNotNull(cryptoTransactionDto);
        assertEquals(cryptoTransaction.getId(), cryptoTransactionDto.getId());
        assertEquals(cryptoTransaction.getUser().getId(), cryptoTransactionDto.getUser().getId());
        assertEquals(cryptoTransaction.getUser().getUsername(), cryptoTransactionDto.getUser().getUsername());
        assertEquals(cryptoTransaction.getCryptoTransactionType(), cryptoTransactionDto.getTransactionType());
        assertEquals(cryptoTransaction.getAmount(), cryptoTransactionDto.getAmount());
        assertEquals(cryptoTransaction.getPrice(), cryptoTransactionDto.getPrice());
        assertEquals(cryptoTransaction.getTransactionDate(), cryptoTransactionDto.getTransactionDate());
        assertEquals(cryptoTransaction.getAmount()
                .multiply(cryptoTransaction.getCryptocurrency().getPrice()), cryptoTransactionDto.getTransactionValue());
        assertEquals(cryptoTransaction.getAmount()
                .multiply(cryptoTransaction.getPrice())
                .divide(currency1.getPrice(), 2, RoundingMode.CEILING), cryptoTransactionDto.getCurrentValue());
        assertEquals(cryptoTransaction.getCreatedAt(), cryptoTransactionDto.getCreatedAt());
        assertEquals(cryptoTransaction.getUpdatedAt(), cryptoTransactionDto.getUpdatedAt());
        assertEquals(crypto1.getName(), cryptoTransactionDto.getCryptocurrencyName());
        assertEquals(crypto1.getSymbol(), cryptoTransactionDto.getCryptocurrencySymbol());
    }

    @Test
    void shouldMapToCryptoTransactionDtoList() {
        //given
        CryptoTransaction cryptoTransaction1 = CryptoTransaction.builder()
                .id(1L)
                .user(user1)
                .cryptocurrency(crypto1)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        CryptoTransaction cryptoTransaction2 = CryptoTransaction.builder()
                .id(2L)
                .user(user2)
                .cryptocurrency(crypto2)
                .cryptoTransactionType(CryptoTransactionType.SELL)
                .amount(BigDecimal.TWO)
                .price(BigDecimal.TWO)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        List<CryptoTransaction> cryptoTransactionList = List.of(cryptoTransaction1, cryptoTransaction2);
        //when
        List<CryptoTransactionDto> cryptoTransactionDtoList = cryptoTransactionMapper.mapToCryptoTransactionDtoList(cryptoTransactionList);
        //then
        assertNotNull(cryptoTransactionDtoList);
        assertEquals(cryptoTransactionList.size(), cryptoTransactionDtoList.size());
        assertEquals(cryptoTransaction1.getId(), cryptoTransactionDtoList.getFirst().getId());
        assertEquals(cryptoTransaction1.getUser().getId(), cryptoTransactionDtoList.getFirst().getUser().getId());
        assertEquals(cryptoTransaction2.getId(), cryptoTransactionDtoList.getLast().getId());
        assertEquals(cryptoTransaction2.getUser().getId(), cryptoTransactionDtoList.getLast().getUser().getId());
    }

    @Test
    void shouldMapToCryptoTransactionRequest() {
        //given
        CryptoTransaction cryptoTransaction = CryptoTransaction.builder()
                .id(1L)
                .user(user1)
                .cryptocurrency(crypto1)
                .cryptoTransactionType(CryptoTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        CryptoTransactionRequest cryptoTransactionRequest = cryptoTransactionMapper.mapToCryptoTransactionRequest(cryptoTransaction);
        //then
        assertNotNull(cryptoTransactionRequest);
        assertEquals(cryptoTransaction.getId(), cryptoTransactionRequest.getId());
        assertEquals(cryptoTransaction.getUser().getId(), cryptoTransactionRequest.getUserId());
        assertEquals(cryptoTransaction.getCryptoTransactionType(), cryptoTransactionRequest.getTransactionType());
        assertEquals(cryptoTransaction.getAmount(), cryptoTransactionRequest.getAmount());
        assertEquals(cryptoTransaction.getPrice(), cryptoTransactionRequest.getPrice());
        assertEquals(cryptoTransaction.getTransactionDate(), cryptoTransactionRequest.getTransactionDate());
        assertEquals(cryptoTransaction.getCryptocurrency().getSymbol(), cryptoTransactionRequest.getCryptocurrency());
        assertEquals(cryptoTransaction.getCryptocurrency().getName(), cryptoTransactionRequest.getName());
    }
}