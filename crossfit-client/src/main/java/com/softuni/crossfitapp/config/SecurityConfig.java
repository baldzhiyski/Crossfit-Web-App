package com.softuni.crossfitapp.config;

import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import com.softuni.crossfitapp.service.oath.OAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           OAuthSuccessHandler oAuthSuccessHandler) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Allow static resources
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // Allow access to specific URLs based on roles
                        .requestMatchers("/", "/users/login", "/users/register", "/users/login-error", "/coaches","/access-denied","/nutrition-blog","/users/activate/{activation_code}","/users/last-register-step").permitAll()
                        .requestMatchers("/memberships/explore").hasRole("USER")
                        .requestMatchers("/workouts/details/{trainingType}/comment/dislike/{commentId}").hasRole("USER")
                        .requestMatchers("/workouts/details/{trainingType}/comment/like/{commentId}").hasRole("USER")
                        .requestMatchers("/joinTraining/{trainingId}").hasRole("MEMBER")
                        .requestMatchers("/memberships/checkout/{membershipType}").hasRole("USER")
                        .requestMatchers("/users/profile/{username}").hasRole("USER")
                        .requestMatchers("/workouts").permitAll()
                        .requestMatchers("/users/my-workouts").hasRole("USER")
                        .requestMatchers("/profiles-dashboard/{id}").hasRole("ADMIN")
                        .requestMatchers("/weekly-schedule/{username}").hasRole("COACH")
                        .requestMatchers("/about-us").permitAll()
                        .requestMatchers("/users/updateAcc").hasRole("USER")
                        .requestMatchers("/users/add-event").hasRole("USER")
                        .requestMatchers("/schedule-for-the-week").hasRole("MEMBER")
                        .requestMatchers("/my-weekly-schedule/{id}").hasRole("COACH")
                        .requestMatchers("/crossfit/api/convert").permitAll()
                        .requestMatchers("/workouts/explore-current/{trainingsType}").hasRole("USER")
                        .requestMatchers("/workouts/details/comment/{trainingsType}").hasRole("USER")
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
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
                .oauth2Login(
                        oauth -> oauth.successHandler(oAuthSuccessHandler)
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
