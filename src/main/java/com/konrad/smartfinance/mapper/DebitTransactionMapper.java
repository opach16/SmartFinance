package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.DebitTransactionDto;
import com.konrad.smartfinance.domain.dto.DebitTransactionRequest;
import com.konrad.smartfinance.domain.model.DebitTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebitTransactionMapper {

    private final UserMapper userMapper;

    public DebitTransactionDto mapToDebitTransactionDto(DebitTransaction transaction) {
        return DebitTransactionDto.builder()
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

    public List<DebitTransactionDto> mapToDebitTransactionDtoList(List<DebitTransaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToDebitTransactionDto)
                .toList();
    }

    public DebitTransactionRequest mapToDebitTransactionRequest(DebitTransaction transaction) {
        return DebitTransactionRequest.builder()
                .transactionId(transaction.getId())
                .userId(transaction.getUser().getId())
                .transactionType(transaction.getTransactionType())
                .name(transaction.getName())
                .currency(transaction.getUser().getAccount().getMainCurrency().getSymbol())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
