package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.ReCaptchaResponseDTO;

import java.util.Optional;

public interface ReCaptchaService {
    Optional<ReCaptchaResponseDTO> verify(String token);
}