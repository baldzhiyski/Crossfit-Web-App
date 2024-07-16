package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.exceptions.AccessOnlyForCoaches;
import com.softuni.crossfitapp.exceptions.FileStorageException;
import com.softuni.crossfitapp.exceptions.FullTrainingCapacityException;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleFileStorageException(FileStorageException ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error"); // Name of the error page view
        model.addAttribute("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(FullTrainingCapacityException.class)
    public ModelAndView handleTryToJoinFullSession(){
        return new ModelAndView("redirect:/access-denied");
    }

    @ExceptionHandler(AccessOnlyForCoaches.class)
    public ModelAndView handleAccessOnlyForCoachesException(){
        return new ModelAndView("redirect:/access-denied");
    }
}
