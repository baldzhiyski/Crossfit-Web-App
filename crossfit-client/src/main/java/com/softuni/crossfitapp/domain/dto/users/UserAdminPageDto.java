package com.softuni.crossfitapp.domain.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserAdminPageDto {

    private UUID uuid;
    private String username;
    private String email;
    private boolean isDisabled;
}
