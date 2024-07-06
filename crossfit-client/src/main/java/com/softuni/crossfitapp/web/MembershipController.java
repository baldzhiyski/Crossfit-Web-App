package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.MembershipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/memberships")
public class MembershipController {

    private ExchangeRateService exchangeRateService;
    private MembershipService membershipService;

    public MembershipController(ExchangeRateService exchangeRateService, MembershipService membershipService) {
        this.exchangeRateService = exchangeRateService;
        this.membershipService = membershipService;
    }


    @GetMapping("/explore")
    public String memberships(Model model){
        if(!model.containsAttribute("allCurrencies")){
            model.addAttribute("allCurrencies",this.exchangeRateService.allSupportedCurrencies());
        }

        if(!model.containsAttribute("membershipDtos")){
            model.addAttribute("membershipDtos",this.membershipService.getMemberships());
        }
        return "memberships";
    }

    @GetMapping("/checkout/{memberShipId}")
    public String getToCheckOut(@PathVariable UUID memberShipId){
        return "card-payment";
    }

    //TODO : Impl Post so logged user can buy the membership he/she wants
}
