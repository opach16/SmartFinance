package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.CryptoTransactionType;
import com.konrad.smartfinance.domain.dto.CryptoTransactionDto;
import com.konrad.smartfinance.domain.dto.CryptoTransactionRequest;
import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.*;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
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
class CryptoTransactionMapperTestSuite {

    private Currency currency1;
    private Currency currency2;
    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;
    private Account account1;
    private Account account2;
    private Cryptocurrency crypto1;
    private Cryptocurrency crypto2;

    @InjectMocks
    private CryptoTransactionMapper cryptoTransactionMapper;

    @Mock
    private CryptocurrencyMapper cryptocurrencyMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CryptocurrencyRepository cryptocurrencyRepository;

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
                .user(user1)
                .mainCurrency(currency2)
                .mainBalance(new BigDecimal("400"))
                .build();
        user1.setAccount(account1);
        user2.setAccount(account2);

        crypto1 = Cryptocurrency.builder()
                .id(1L)
                .symbol("testSymbol1")
                .name("testName1")
                .price(BigDecimal.ONE)
                .build();
        crypto2 = Cryptocurrency.builder()
                .id(2L)
                .symbol("testSymbol2")
                .name("testName2")
                .price(BigDecimal.TWO)
                .build();
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
        when(cryptocurrencyRepository.findById(crypto1.getId())).thenReturn(Optional.of(crypto1));
        when(userMapper.mapToUserDto(user1)).thenReturn(userDto1);
        when(cryptocurrencyMapper.mapToCryptocurrencyDto(crypto1))
                .thenReturn(CryptocurrencyDto.builder().id(1L).price(crypto1.getPrice()).build());
        //when
        CryptoTransactionDto cryptoTransactionDto = cryptoTransactionMapper.mapToCryptoTransactionDto(cryptoTransaction);
        //then
        assertNotNull(cryptoTransactionDto);
        assertEquals(cryptoTransaction.getId(), cryptoTransactionDto.getId());
        assertEquals(cryptoTransaction.getUser().getId(), cryptoTransactionDto.getUser().getId());
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
        when(cryptocurrencyRepository.findById(crypto1.getId())).thenReturn(Optional.of(crypto1));
        when(cryptocurrencyRepository.findById(crypto2.getId())).thenReturn(Optional.of(crypto2));
        when(userMapper.mapToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.mapToUserDto(user2)).thenReturn(userDto2);
        when(cryptocurrencyMapper.mapToCryptocurrencyDto(crypto1))
                .thenReturn(CryptocurrencyDto.builder().id(1L).price(crypto1.getPrice()).build());
        when(cryptocurrencyMapper.mapToCryptocurrencyDto(crypto2))
                .thenReturn(CryptocurrencyDto.builder().id(2L).price(crypto2.getPrice()).build());
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