package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

    private final CryptocurrencyRepository cryptocurrencyRepository;

    public List<Cryptocurrency> getAll() {
        return cryptocurrencyRepository.findAll();
    }

    public Cryptocurrency getBySymbol(String symbol) throws CryptocurrencyException {
        return cryptocurrencyRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
    }

    public BigDecimal getPrice(String symbol) throws CryptocurrencyException {
        Cryptocurrency fetchedCryptocurrency = cryptocurrencyRepository.findBySymbol(symbol).orElseThrow(() -> new CryptocurrencyException(CryptocurrencyException.NOT_FOUND));
        return fetchedCryptocurrency.getPrice();
    }
}
