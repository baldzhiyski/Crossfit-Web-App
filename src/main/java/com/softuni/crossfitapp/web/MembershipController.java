package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/memberships")
public class MembershipController {


    @GetMapping("/checkout")
    public String toCheckOut(){
        return "card-payment";
    }

    @GetMapping("/memberships/checkout/{memberShipId}")
    public String getToCheckOut(@PathVariable UUID memberShipId){
        return "card-payment";
    }

    //TODO : Impl Post so logged user can buy the membership he/she wants
}
