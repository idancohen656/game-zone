package com.example.game_zone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
//dto class for the response of the Tic Tac Toe game
public class TicTacToeResponseDto {
    private String[] board;
    private Long nextPlayer;
    private String winner;
    private List<Integer> winningCells;
    private String status;
}
