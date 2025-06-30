package com.example.game_zone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//dto class to hold game session data
public class GameSessionDto {
    private Long player1Id;
    private Long player2Id;
    private String player1Name;
    private String player2Name;
    private Long SessionId;
}
