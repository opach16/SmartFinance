package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.CurrencyDto;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.CurrencyMapper;
import com.konrad.smartfinance.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok().body(currencyMapper.mapToCurrencyDtoList(currencyService.getAllCurrencies()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CurrencyDto>> getAllCurrenciesByUserId(@PathVariable Long userId) throws UserException {
        return ResponseEntity.ok().body(currencyMapper.
                mapToCurrencyDtoList(currencyService.getAllCurrenciesByUserId(userId)));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CurrencyDto> getCurrencyBySymbol(@PathVariable String symbol) throws CurrencyExeption {
        return ResponseEntity.ok().body(currencyMapper.mapToCurrencyDto(currencyService.getCurrencyBySymbol(symbol)));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getCurrencyPrice(@PathVariable String symbol) throws CurrencyExeption {
        return ResponseEntity.ok().body(currencyService.getCurrencyPrice(symbol));
    }
}
