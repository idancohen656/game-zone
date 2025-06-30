package com.example.game_zone.dto;

import lombok.Data;

@Data
//dto for starting request
public class StartGameRequest {
    private Long sessionId;
    private Long playerId;
}
