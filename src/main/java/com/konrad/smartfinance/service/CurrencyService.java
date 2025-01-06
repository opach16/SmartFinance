package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CurrencyapiClient;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyapiClient currencyapiClient;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency getCurrencyBySymbol(String symbol) throws CurrencyExeption {
        return currencyRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
    }

    public BigDecimal getCurrencyPrice(String symbol) throws CurrencyExeption {
        Currency fetchedCurrency = currencyRepository.findBySymbol(symbol).orElseThrow(() -> new CurrencyExeption(CurrencyExeption.CURRENCY_NOT_FOUND));
        return fetchedCurrency.getPrice();
    }

    public void updateCurrencies() {
        List<Currency> currencies = currencyapiClient.fetchCurrencies();
        for (Currency currency : currencies) {
            Currency fetchedCurrency = currencyRepository.findBySymbol(currency.getSymbol()).orElse(null);
            if (fetchedCurrency != null) {
                fetchedCurrency.setPrice(currency.getPrice());
                currencyRepository.save(fetchedCurrency);
            } else {
                currencyRepository.save(currency);
            }
        }
    }
}
