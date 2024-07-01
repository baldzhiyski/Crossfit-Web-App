package com.softuni.crossfitapp.web.rest;

import com.softuni.crossfitapp.domain.dto.exrates.ConversionResultDTO;
import com.softuni.crossfitapp.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyController {
    private final ExchangeRateService exRateService;

    public CurrencyController(ExchangeRateService exRateService) {
        this.exRateService = exRateService;
    }

    @GetMapping("/crossfit/api/convert")
    public ResponseEntity<ConversionResultDTO> convert(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("amount") BigDecimal amount
    ) {
        BigDecimal result = exRateService.convert(from, to, amount);

        return ResponseEntity.ok(new ConversionResultDTO(
                from,
                to,
                amount,
                result
        ));
    }
}
