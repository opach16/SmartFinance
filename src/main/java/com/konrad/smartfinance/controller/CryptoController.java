package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CryptocurrencyDto;
import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CryptocurrencyMapper;
import com.konrad.smartfinance.service.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    private final CryptocurrencyService cryptocurrencyService;
    private final CryptocurrencyMapper cryptocurrencyMapper;

    @GetMapping
    public ResponseEntity<List<CryptocurrencyDto>> getAllCryptocurrencies() {
        cryptocurrencyService.updateCryptocurrencies();
        return ResponseEntity.ok().body(cryptocurrencyMapper.mapToCryptocurrencyDtoList(cryptocurrencyService.getAll()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CryptocurrencyDto>> getAllCryptocurrenciesById(@PathVariable Long userId) throws UserException {
        cryptocurrencyService.updateCryptocurrencies();
        return ResponseEntity.ok().body(cryptocurrencyMapper
                .mapToCryptocurrencyDtoList(cryptocurrencyService.getAllCryptocurrenciesByUserId(userId)));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptocurrencyDto> getCryptocurrenciesByUserId(@PathVariable String symbol) throws CryptocurrencyException {
        return ResponseEntity.ok().body(cryptocurrencyMapper.mapToCryptocurrencyDto(cryptocurrencyService.getBySymbol(symbol)));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getPriceBySymbol(@PathVariable String symbol) throws CryptocurrencyException {
        return ResponseEntity.ok().body(cryptocurrencyService.getPrice(symbol));
    }
}
