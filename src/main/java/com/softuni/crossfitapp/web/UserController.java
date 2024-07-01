package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.service.CountryService;
import com.softuni.crossfitapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static com.softuni.crossfitapp.util.Constants.BINDING_RESULT_PATH;
import static com.softuni.crossfitapp.util.Constants.DOT;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private CountryService countryService;

    public UserController(UserService userService, CountryService countryService) {
        this.userService = userService;
        this.countryService = countryService;
    }


    @GetMapping("/login")
    public String login(Model model) {
        if(!model.containsAttribute("username")){
            model.addAttribute("username","");
        }
        return "auth-login";
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model){
        if(!model.containsAttribute("registerDto")){
            model.addAttribute("registerDto", new UserRegisterDto());
        }

        if(!model.containsAttribute("countryCodes")){
            model.addAttribute("countryCodes", this.countryService.allCountryCodes());
        }

        return "auth-register";

    }


    @PostMapping("/register")
    public ModelAndView doRegister(@Valid UserRegisterDto userRegisterDto, BindingResult bindingResult , RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            final String attributeName = "registerDto";
            redirectAttributes
                    .addFlashAttribute("registerDto", userRegisterDto)
                    .addFlashAttribute(BINDING_RESULT_PATH + DOT + attributeName, bindingResult);
            modelAndView.setViewName("redirect:/users/register");
            return modelAndView;
        }

        userService.registerNewUser(userRegisterDto);
        redirectAttributes.addFlashAttribute("successMessage", "Successfully registered ! Please Log In !");
        modelAndView.setViewName("redirect:/users/login");
        return modelAndView;
    }

    @PostMapping("/login-error")
    public String onFailure(
            Model model) {

        model.addAttribute("badRequest", "Invalid username or password !");

        return "auth-login";
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


    //TODO: With some path-variable
    @GetMapping("/my-trainings")
    public String myTrainings(){
        return "my-trainings";
    }

}
