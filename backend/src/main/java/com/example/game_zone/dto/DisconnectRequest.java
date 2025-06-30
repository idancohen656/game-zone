package com.example.game_zone.dto;

import lombok.Data;

@Data
// dto class for disconnect request
public class DisconnectRequest {
    private Long sessionId;
    private Long playerId;
}
