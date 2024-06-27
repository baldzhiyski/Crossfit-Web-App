package com.softuni.crossfitapp.config;

import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final String rememberMeKey;



    public SecurityConfig(@Value("${remember_me_key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Allow static resources
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // Allow access to specific URLs based on roles
                        .requestMatchers("/", "/users/login", "/users/register", "/users/login-error", "/access-denied").permitAll()
                        .requestMatchers("/memberships").hasRole("USER")
                        .requestMatchers("/users/profile").hasRole("USER")
                        .requestMatchers("/workouts").permitAll()
                        .requestMatchers("/users/my-workouts").hasRole("USER")
                        .requestMatchers("/profiles-dashboard").hasRole("ADMIN")
                        .requestMatchers("/weekly-schedule").hasRole("COACH")
                        .requestMatchers("/about-us").permitAll()
                        .requestMatchers("/users/updateAcc").hasRole("USER")
                        .requestMatchers("/users/add-event").hasRole("USER")
                        .requestMatchers("/schedule").hasRole("MEMBER")
                        // Catch-all for any other requests, must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureForwardUrl("/users/login-error")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                .rememberMe(rememberMe -> rememberMe
                        .key(rememberMeKey)
                        .rememberMeParameter("rememberme")
                        .rememberMeCookieName("rememberme")
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customHandler())
                );

        return httpSecurity.build();
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CrossfitUserDetailsService(userRepository);
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler customHandler() {
        return new CustomAccessDeniedHandler();
    }


}
