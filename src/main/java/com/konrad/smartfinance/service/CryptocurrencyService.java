package com.konrad.smartfinance.service;

import com.konrad.smartfinance.client.CoingeckoClient;
import com.konrad.smartfinance.domain.model.Cryptocurrency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.CryptocurrencyRepository;
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
public class CryptocurrencyService {

    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CoingeckoClient coingeckoClient;
    private final UserRepository userRepository;

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

    @Scheduled(cron = "0 0 5 * * ?")
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

    public List<Cryptocurrency> getAllCryptocurrenciesByUserId(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        BigDecimal mainCurrencyPrice = user.getAccount().getMainCurrency().getPrice();
        return cryptocurrencyRepository.findAll().stream()
                .map(crypto -> {
                    return Cryptocurrency.builder()
                            .id(crypto.getId())
                            .symbol(crypto.getSymbol())
                            .name(crypto.getName())
                            .price(crypto.getPrice().divide(mainCurrencyPrice, 2, RoundingMode.CEILING))
                            .createdAt(crypto.getCreatedAt())
                            .updatedAt(crypto.getUpdatedAt())
                            .build();
                })
                .toList();
    }

    @PostConstruct
    public void init() {
        updateCryptocurrencies();
    }
}
