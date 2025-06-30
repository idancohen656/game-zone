package com.example.game_zone.dto;
import lombok.Data;
@Data
//dto class to hold game request data
public class GameRequestDto {
    private Long playerId;
    private String gameName;
}
