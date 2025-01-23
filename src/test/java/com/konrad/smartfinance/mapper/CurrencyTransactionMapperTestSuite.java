package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.CurrencyTransactionType;
import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionDto;
import com.konrad.smartfinance.domain.dto.CurrencyTransactionRequest;
import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.CurrencyTransaction;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyTransactionMapperTestSuite {

    private Currency currency1;
    private Currency currency2;
    private UserDto userDto1;
    private UserDto userDto2;
    private User user1;
    private User user2;
    private Account account1;
    private Account account2;

    @InjectMocks
    private CurrencyTransactionMapper currencyTransactionMapper;

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currency1 = Currency.builder()
                .id(1L)
                .symbol("USD")
                .price(new BigDecimal("5"))
                .build();
        currency2 = Currency.builder()
                .id(2L)
                .symbol("EUR")
                .price(new BigDecimal("4"))
                .build();
        userDto1 = UserDto.builder()
                .id(1L)
                .build();
        userDto2 = UserDto.builder()
                .id(2L)
                .build();
        user1 = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        user2 = User.builder()
                .id(2L)
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
                .user(user2)
                .mainCurrency(currency2)
                .mainBalance(new BigDecimal("400"))
                .build();
        user1.setAccount(account1);
        user2.setAccount(account2);
    }

    @Test
    void shouldMapToCurrencyTransactionDto() {
        //given
        CurrencyTransaction currencyTransaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user2)
                .currency(currency1)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        when(currencyRepository.findBySymbol(currency1.getSymbol())).thenReturn(Optional.of(currency1));
        when(userMapper.mapToUserDto(user2)).thenReturn(userDto2);
        when(currencyMapper.mapToCurrencyDto(currency1)).thenReturn(CurrencyDto.builder().id(1L).price(currency1.getPrice()).build());
        //when
        CurrencyTransactionDto currencyTransactionDto = currencyTransactionMapper.mapToCurrencyTransactionDto(currencyTransaction);
        //then
        assertNotNull(currencyTransactionDto);
        assertEquals(currencyTransaction.getId(), currencyTransactionDto.getId());
        assertEquals(currencyTransaction.getUser().getId(), currencyTransactionDto.getUser().getId());
        assertEquals(currencyTransaction.getCurrencyTransactionType(), currencyTransactionDto.getTransactionType());
        assertEquals(currencyTransaction.getAmount(), currencyTransactionDto.getAmount());
        assertEquals(currencyTransaction.getPrice(), currencyTransactionDto.getPrice());
        assertEquals(currencyTransaction.getTransactionDate(), currencyTransactionDto.getTransactionDate());
        assertEquals(currencyTransaction.getAmount()
                .multiply(currencyTransaction.getPrice()), currencyTransactionDto.getTransactionValue());
        assertEquals(currencyTransaction.getAmount()
                .multiply(currencyTransaction.getCurrency().getPrice())
                .divide(currency2.getPrice(), 2, RoundingMode.CEILING), currencyTransactionDto.getCurrentValue());
        assertEquals(currencyTransaction.getCreatedAt(), currencyTransactionDto.getCreatedAt());
        assertEquals(currencyTransaction.getUpdatedAt(), currencyTransactionDto.getUpdatedAt());
        assertEquals(currency1.getSymbol(), currencyTransactionDto.getCurrencySymbol());
    }

    @Test
    void shouldMapToCurrencyTransactionDtoList() {
        //given
        CurrencyTransaction currencyTransaction1 = CurrencyTransaction.builder()
                .id(1L)
                .user(user2)
                .currency(currency1)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(new BigDecimal("100"))
                .price(new BigDecimal("4"))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        CurrencyTransaction currencyTransaction2 = CurrencyTransaction.builder()
                .id(2L)
                .user(user1)
                .currency(currency2)
                .currencyTransactionType(CurrencyTransactionType.SELL)
                .amount(new BigDecimal("50"))
                .price(new BigDecimal("3"))
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        List<CurrencyTransaction> currencyTransactionList = List.of(currencyTransaction1, currencyTransaction2);
        when(currencyRepository.findBySymbol(currency1.getSymbol())).thenReturn(Optional.of(currency1));
        when(currencyRepository.findBySymbol(currency2.getSymbol())).thenReturn(Optional.of(currency2));
        when(userMapper.mapToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.mapToUserDto(user2)).thenReturn(userDto2);
        when(currencyMapper.mapToCurrencyDto(currency1)).thenReturn(CurrencyDto.builder().id(1L).price(currency1.getPrice()).build());
        when(currencyMapper.mapToCurrencyDto(currency2)).thenReturn(CurrencyDto.builder().id(2L).price(currency2.getPrice()).build());
        //when
        List<CurrencyTransactionDto> dtoList = currencyTransactionMapper.mapToCurrencyTransactionDtoList(currencyTransactionList);
        //then
        assertNotNull(dtoList);
        assertEquals(currencyTransactionList.size(), dtoList.size());
        assertEquals(currencyTransaction1.getId(), dtoList.getFirst().getId());
        assertEquals(currencyTransaction1.getUser().getId(), dtoList.getFirst().getUser().getId());
        assertEquals(currencyTransaction1.getCurrency().getId(), dtoList.getFirst().getCurrency().getId());
        assertEquals(currencyTransaction2.getId(), dtoList.getLast().getId());
    }

    @Test
    void shouldMapToCurrencyTransactionRequest() {
        //given
        CurrencyTransaction currencyTransaction = CurrencyTransaction.builder()
                .id(1L)
                .user(user2)
                .currency(currency1)
                .currencyTransactionType(CurrencyTransactionType.BUY)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .transactionDate(LocalDate.of(2020, 1, 1))
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        //when
        CurrencyTransactionRequest currencyTransactionRequest = currencyTransactionMapper.mapToCurrencyTransactionRequest(currencyTransaction);
        assertNotNull(currencyTransactionRequest);
        assertEquals(currencyTransaction.getId(), currencyTransactionRequest.getId());
        assertEquals(currencyTransaction.getUser().getId(), currencyTransactionRequest.getUserId());
        assertEquals(currencyTransaction.getCurrency().getSymbol(), currencyTransactionRequest.getCurrency());
        assertEquals(currencyTransaction.getAmount(), currencyTransactionRequest.getAmount());
        assertEquals(currencyTransaction.getPrice(), currencyTransactionRequest.getPrice());
        assertEquals(currencyTransaction.getTransactionDate(), currencyTransactionRequest.getTransactionDate());
        assertEquals(currencyTransaction.getCurrencyTransactionType(), currencyTransactionRequest.getTransactionType());
    }
}