package com.softuni.crossfitapp.domain.user_details;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class CrossfitUserDetails extends User {
    private UUID uuid;
    private Long id;
    private final String lastName;
    private final String firstName;

    public CrossfitUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String firstName,String lastName,UUID uuid,Long id) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.uuid = uuid;
        this.id =id;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
