package com.example.game_zone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FourInRowMoveReq {
    private Long sessionId;
    private Long playerId;
    private int column;
}
