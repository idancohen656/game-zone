package com.example.game_zone.dto;
import lombok.Data;

@Data
//dto class for tic tac toe move request
public class TicTacToeMoveRequest {
    private Long sessionId;
    private Long playerId;
    private int cellIndex;

}
