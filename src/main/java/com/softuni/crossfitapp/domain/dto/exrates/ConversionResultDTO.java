package com.softuni.crossfitapp.domain.dto.exrates;

import java.math.BigDecimal;

public record ConversionResultDTO(String from, String to, BigDecimal amount, BigDecimal result) {

}