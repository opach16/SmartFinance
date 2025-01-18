package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CurrencyapiClient;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CurrencyRepository;
import com.konrad.smartfinance.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyapiClient currencyapiClient;
    private final UserRepository userRepository;

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

    @Scheduled(cron = "0 0 5 * * ?")
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

    public List<Currency> getAllCurrenciesByUserId(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        BigDecimal mainCurrencyPrice = user.getAccount().getMainCurrency().getPrice();
        return currencyRepository.findAll().stream()
                .map(currency -> {
                    return Currency.builder()
                            .id(currency.getId())
                            .symbol(currency.getSymbol())
                            .price(currency.getPrice().divide(mainCurrencyPrice, 2, RoundingMode.CEILING))
                            .createdAt(currency.getCreatedAt())
                            .updatedAt(currency.getUpdatedAt())
                            .build();
                })
                .toList();
    }

    @PostConstruct
    public void init() {
        updateCurrencies();
    }
}
