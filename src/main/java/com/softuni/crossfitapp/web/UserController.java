package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {


    @GetMapping("/login")
    public String getLogInPage(){
        return "auth-login";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "auth-register";

    }

    //TODO: With some path-variable
    @GetMapping("/updateAcc")
    public String getUpdateAccountPage(){
        return "updateProfilePage";
    }

    //TODO: With some path-variable
    @GetMapping("/profile")
    public String getProfile(){
        return "profile-page";
    }

}
