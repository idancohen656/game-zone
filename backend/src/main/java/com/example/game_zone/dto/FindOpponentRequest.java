package com.example.game_zone.dto;

import lombok.Data;

@Data
// dto class to find opponent request
public class FindOpponentRequest {
    private Long playerId;
    private String gameName;
}
