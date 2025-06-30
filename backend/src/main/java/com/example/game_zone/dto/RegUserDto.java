package com.example.game_zone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// DTO class for user registration response
public class RegUserDto {
    private Long userId;
    private String username;
    private String message;
    }
