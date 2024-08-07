package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.ReCaptchaResponseDTO;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.service.CountryService;
import com.softuni.crossfitapp.service.ReCaptchaService;
import com.softuni.crossfitapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private ReCaptchaService reCaptchaService;
    public UserController(UserService userService, CountryService countryService, ReCaptchaService reCaptchaService) {
        this.userService = userService;
        this.countryService = countryService;
        this.reCaptchaService = reCaptchaService;
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
    public ModelAndView doRegister(@Valid UserRegisterDto userRegisterDto, BindingResult bindingResult , RedirectAttributes redirectAttributes,
                                   @RequestParam("g-recaptcha-response") String reCaptchaResponse           ) throws IOException {
        ModelAndView modelAndView = new ModelAndView();



        if (bindingResult.hasErrors()) {
            final String attributeName = "registerDto";
            redirectAttributes
                    .addFlashAttribute("registerDto", userRegisterDto)
                    .addFlashAttribute(BINDING_RESULT_PATH + DOT + attributeName, bindingResult);
            modelAndView.setViewName("redirect:/users/register");
            return modelAndView;
        }
        boolean isBot = !reCaptchaService
                .verify(reCaptchaResponse)
                .map(ReCaptchaResponseDTO::isSuccess)
                .orElse(false);


        if (isBot) {
            modelAndView.setViewName("redirect:/");
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
                                RedirectAttributes redirectAttributes) {

        this.userService.activateAccount(activationCode);
        redirectAttributes.addFlashAttribute("successMessage","Successfully registered ! Please Log In !");
        return "redirect:/users/login";
    }
    @PostMapping("/login-error")
    public String onFailure(
            Model model) {

        model.addAttribute("badRequest", "Invalid username or password !");

        return "auth-login";
    }

    @GetMapping("/profile/{username}")
    public String getProfile(@PathVariable String username,Model model){

        model.addAttribute("userProfileDto",this.userService.getProfilePageDto(username));

        return "profile-page";
    }

    @GetMapping("/profile/{username}/edit")
    public String toEditPage(@PathVariable String username,Model model){

        if(!model.containsAttribute("userProfileUpdateDto")){
            model.addAttribute("userProfileUpdateDto",new UserProfileUpdateDto());
        }

        return "updateProfilePage";
    }

    @PatchMapping("/profile/{username}/edit")
    public ModelAndView doUpdate(@PathVariable String username, @Valid UserProfileUpdateDto userProfileUpdateDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException, ServletException {
        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            final String attributeName = "userProfileUpdateDto";
            redirectAttributes
                    .addFlashAttribute("userProfileUpdateDto", userProfileUpdateDto)
                    .addFlashAttribute(BINDING_RESULT_PATH + DOT + attributeName, bindingResult);
            modelAndView.setViewName("redirect:/users/profile/" + username + "/edit");
            return modelAndView;
        }
        this.userService.updateProfile(username,userProfileUpdateDto);

        request.getSession().invalidate();
        modelAndView.setViewName("redirect:/users/login");
        return modelAndView;
    }

    @DeleteMapping("/profile/{username}/edit/delete-profile")
    public String deleteAcc(@PathVariable String username){

        this.userService.deleteAcc(username);
        return "redirect:/";
    }

    @GetMapping("/my-trainings")
    public String myTrainings(){
        return "my-trainings";
    }

}
