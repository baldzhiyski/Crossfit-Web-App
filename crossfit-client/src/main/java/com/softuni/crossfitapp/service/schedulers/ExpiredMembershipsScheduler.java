package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ExpiredMembershipsScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void processExpiredMemberships() {
        // Get all users or a subset where membership is expired
        Set<User> usersWithExpiredMembership = userService.findAllUsersWithExpiredMembership();

         for (User user : usersWithExpiredMembership) {
             userService.removeExpiredMembership(user);
         }
    }
}
