package com.softuni.crossfitapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(Include.NON_NULL)
public class ReCaptchaResponseDTO {

    private boolean success;
    private List<String> errorCodes;

    public ReCaptchaResponseDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ReCaptchaResponseDTO setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
        return this;
    }
}