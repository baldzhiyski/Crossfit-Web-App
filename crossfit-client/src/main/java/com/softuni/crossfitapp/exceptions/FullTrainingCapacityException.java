package com.softuni.crossfitapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class FullTrainingCapacityException extends RuntimeException{

    public FullTrainingCapacityException(String message) {
        super(message);

    }
}
