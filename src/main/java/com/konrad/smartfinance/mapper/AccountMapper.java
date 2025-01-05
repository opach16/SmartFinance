package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.dto.AccountTransactionDto;
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

    public Account mapToAccount(AccountDto accountDto) {
        return Account.builder()
                .user(userMapper.mapToUserEntity(accountDto.getUser()))
                .mainCurrency(currencyMapper.mapToCurrencyEntity(accountDto.getMainCurrency()))
                .mainBalance(accountDto.getMainBalance())
                .assetsBalance(accountDto.getAssetsBalance())
                .build();
    }

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

    public AccountTransaction mapToAccountTransaction(AccountTransactionDto transactionDto) {
        return AccountTransaction.builder()
                .user(userMapper.mapToUserEntity(transactionDto.getUser()))
                .transactionType(transactionDto.getTransactionType())
                .name(transactionDto.getName())
                .currency(currencyMapper.mapToCurrencyEntity(transactionDto.getCurrency()))
                .amount(transactionDto.getAmount())
                .transactionDate(transactionDto.getTransactionDate())
                .build();
    }

    public AccountTransactionDto mapToAccountTransactionDto(AccountTransaction transaction) {
        return AccountTransactionDto.builder()
                .id(transaction.getId())
                .user(userMapper.mapToUserDto(transaction.getUser()))
                .transactionType(transaction.getTransactionType())
                .name(transaction.getName())
                .currency(currencyMapper.mapToCurrencyDto(transaction.getCurrency()))
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public List<AccountTransaction> mapToAccountTransactionList(List<AccountTransactionDto> transactionDtoList) {
        return transactionDtoList.stream()
                .map(this::mapToAccountTransaction)
                .toList();
    }

    public List<AccountTransactionDto> mapToAccountTransactionDtoList(List<AccountTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToAccountTransactionDto)
                .toList();
    }
}
