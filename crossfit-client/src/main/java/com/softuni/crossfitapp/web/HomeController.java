package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.service.EventService;
import com.softuni.crossfitapp.service.MonitoringService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private EventService eventService;

    private MonitoringService monitoringService;

    public HomeController(EventService eventService, MonitoringService monitoringService) {
        this.eventService = eventService;
        this.monitoringService = monitoringService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model){
        if(userDetails instanceof CrossfitUserDetails crossfitUserDetails){
            model.addAttribute("fullName",crossfitUserDetails.getFullName());
        }
        return "index";
    }


    @GetMapping("/about-us")
    public String about(Model model,@AuthenticationPrincipal UserDetails crossfitUserDetails){
        // Create a GrantedAuthority object for the role "ADMIN"
        GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        // Check if the user has the "ADMIN" role
        boolean isAdmin = crossfitUserDetails.getAuthorities().contains(adminAuthority);

        model.addAttribute("isAdmin", isAdmin);
        return "about";
    }


    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/nutrition-blog")
    public String getTips(){
        monitoringService.increaseBlogReaders();
        return "nutrition-tips";
    }
}
