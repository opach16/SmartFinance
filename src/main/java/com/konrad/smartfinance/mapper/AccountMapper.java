package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.dto.AccountTransactionDto;
import com.konrad.smartfinance.domain.dto.AccountTransactionRequest;
import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.AccountTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountMapper {

    private final UserMapper userMapper;
    private final CurrencyMapper currencyMapper;

    public AccountDto mapToAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .user(userMapper.mapToUserDto(account.getUser()))
                .mainCurrency(currencyMapper.mapToCurrencyDto(account.getMainCurrency()))
                .mainBalance(account.getMainBalance())
                .assetsBalance(account.getAssetsBalance())
                .totalBalance(account.getMainBalance().add(account.getAssetsBalance()))
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public AccountTransactionDto mapToAccountTransactionDto(AccountTransaction transaction) {
        return AccountTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .transactionType(transaction.getTransactionType())
                .name(transaction.getName())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionValue(transaction.getAmount().multiply(transaction.getPrice()))
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .currencySymbol(transaction.getUser().getAccount().getMainCurrency().getSymbol())
                .build();
    }

    public List<AccountTransactionDto> mapToAccountTransactionDtoList(List<AccountTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToAccountTransactionDto)
                .toList();
    }

    public AccountTransactionRequest mapToAccountTransactionRequest(AccountTransaction transaction) {
        return AccountTransactionRequest.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .currency(transaction.getUser().getAccount().getMainCurrency().getSymbol())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
