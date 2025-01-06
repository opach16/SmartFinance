package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CoingeckoClient;
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
    private final CoingeckoClient coingeckoClient;

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

    public void updateCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = coingeckoClient.fetchCryptocurrencies();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            Cryptocurrency fetchedCryptocurrency = cryptocurrencyRepository.findBySymbol(cryptocurrency.getSymbol()).orElse(null);
            if (fetchedCryptocurrency != null) {
                fetchedCryptocurrency.setPrice(cryptocurrency.getPrice());
                cryptocurrencyRepository.save(fetchedCryptocurrency);
            } else {
                cryptocurrencyRepository.save(cryptocurrency);
            }
        }
    }
}
