package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler({ObjectNotFoundException.class, NoResourceFoundException.class})
    public ModelAndView handleNotFound(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("not-found");
        return modelAndView;
    }
}
