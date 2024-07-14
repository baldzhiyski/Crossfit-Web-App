package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.PaymentDto;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.MembershipService;
import com.softuni.crossfitapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.UUID;

import static com.softuni.crossfitapp.util.Constants.BINDING_RESULT_PATH;
import static com.softuni.crossfitapp.util.Constants.DOT;

@Controller
@RequestMapping("/memberships")
public class MembershipController {

    private ExchangeRateService exchangeRateService;
    private MembershipService membershipService;

    private UserService userService;

    public MembershipController(ExchangeRateService exchangeRateService, MembershipService membershipService, UserService userService) {
        this.exchangeRateService = exchangeRateService;
        this.membershipService = membershipService;
        this.userService = userService;
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

    @GetMapping("/checkout/{membershipType}")
    public String getToCheckOut(@PathVariable MembershipType membershipType, Model model, @AuthenticationPrincipal UserDetails userDetails){
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Check if the user has the 'MEMBER' role
        boolean isMember = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MEMBER"));

        if (isMember) {
            // Redirect to access denied page or another appropriate page
            return "redirect:/access-denied";
        }

        if(!model.containsAttribute("membershipDto")){
            model.addAttribute("membershipDto",this.membershipService.getMembershipDto(membershipType));
        }
        if(!model.containsAttribute("paymentDto")){
            model.addAttribute("paymentDto",new PaymentDto());
        }
        return "card-payment";
    }

    @PostMapping("/checkout/{membershipType}")
    public ModelAndView buyMembership(@PathVariable MembershipType membershipType, @Valid PaymentDto paymentDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            final String attributeName = "paymentDto";
            redirectAttributes
                    .addFlashAttribute("paymentDto", paymentDto)
                    .addFlashAttribute(BINDING_RESULT_PATH + DOT + attributeName, bindingResult);
            modelAndView.setViewName("redirect:/memberships/checkout/" + membershipType);
            return modelAndView;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.setViewName("redirect:/users/profile/" + authentication.getName());
        this.userService.buyMembership(authentication.getName(),membershipType);
        return modelAndView;
    }
}
