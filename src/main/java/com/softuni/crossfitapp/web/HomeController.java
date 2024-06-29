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

    @GetMapping("/schedule-for-the-week")
    public String schedule(){
        return "schedule";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/nutrition-blog")
    public String getTips(){
        return "nutrition-tips";
    }
}
