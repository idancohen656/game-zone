package com.example.game_zone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//dto for user login and registration
public class UserDto {
    private String username;
    private String password;
    private String email;
}