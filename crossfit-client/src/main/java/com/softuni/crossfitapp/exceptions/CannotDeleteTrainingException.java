package com.softuni.crossfitapp.exceptions;

public class CannotDeleteTrainingException extends RuntimeException{
    public CannotDeleteTrainingException(String message) {
        super(message);
    }
}
