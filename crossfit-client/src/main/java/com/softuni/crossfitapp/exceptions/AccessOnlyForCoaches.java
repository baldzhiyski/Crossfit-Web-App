package com.softuni.crossfitapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AccessOnlyForCoaches extends RuntimeException {

    public AccessOnlyForCoaches(String message) {
        super(message);
    }
}
