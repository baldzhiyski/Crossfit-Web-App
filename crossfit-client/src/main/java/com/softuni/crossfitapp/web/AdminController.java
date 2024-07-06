package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class AdminController {

    @GetMapping("/profiles-dashboard/{id}")
    public String dashboard(@PathVariable UUID id){
        return "dashboard";
    }

    // TODO :  A delete request to disable users account for specific violations
    // TODO : Dynamic data for updating the values of the chart
}
