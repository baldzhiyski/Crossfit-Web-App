package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.service.CountryService;
import com.softuni.crossfitapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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


    // TODO : Redirect here to a page where it says that u need to confirm the email and from there redirect to the log in page !
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
        modelAndView.setViewName("redirect:/users/last-register-step");
        return modelAndView;
    }

    @GetMapping("/last-register-step")
    public String confirmTab(){
        return "confirm-email";
    }

    @GetMapping("/activate/{activation_code}")
    public String activateUser(@PathVariable("activation_code") String activationCode,
                               Model model) {

        System.out.println("TEST");
        this.userService.activateAccount(activationCode);
        model.addAttribute("successMessage","Successfully registered ! Please Log In !");
        return "redirect:/users/login";
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
