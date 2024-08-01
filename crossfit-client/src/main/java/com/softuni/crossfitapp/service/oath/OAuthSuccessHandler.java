package com.softuni.crossfitapp.service.oath;

import com.softuni.crossfitapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private UserService userService;

    public OAuthSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        if (authentication instanceof OAuth2AuthenticationToken token) {

            OAuth2User user = token.getPrincipal();

            String email = user
                    .getAttribute("email");
            String fullName = user
                    .getAttribute("name");
            String username = user.getAttribute("login");

            String address= !user.getAttribute("location").toString().isBlank() ?user.getAttribute("location").toString() : "Random Adress";

            userService.createUserIfNotExist(username,email, fullName,address);
            authentication = userService.login(username);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}