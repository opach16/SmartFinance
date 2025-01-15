package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.AccountDto;
import com.konrad.smartfinance.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
