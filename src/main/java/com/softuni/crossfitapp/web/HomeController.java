package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model){
        if(userDetails instanceof CrossfitUserDetails crossfitUserDetails){
            model.addAttribute("fullName",crossfitUserDetails.getFullName());
        }
        return "index";
    }


    @GetMapping("/about-us")
    public String about(Model model){
        return "about";
    }

    @GetMapping("/memberships")
    public String memberships(Model model){
        return "memberships";
    }

    @GetMapping("/coaches")
    public String meetTheTeam(){
        return "coaches-page";
    }

    @GetMapping("/schedule")
    public String schedule(){
        return "schedule";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }
}
