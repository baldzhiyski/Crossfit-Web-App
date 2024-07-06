package com.softuni.crossfitapp.service;

import java.io.FileNotFoundException;

public interface SeedService {


    void seedRoles() throws FileNotFoundException;

    void seedCoaches() throws FileNotFoundException;

    void seedMemberships() throws FileNotFoundException;

    void seedCoachesUserAccount() throws FileNotFoundException;

    void seedAdmins() throws FileNotFoundException;

    default void seedAll() throws FileNotFoundException {
        seedRoles();
        seedMemberships();
        seedCoaches();
        seedCoachesUserAccount();
//        seedAdmins();
    }
}
