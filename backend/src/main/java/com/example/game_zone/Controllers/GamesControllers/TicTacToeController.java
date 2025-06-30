package com.example.game_zone.Controllers.GamesControllers;

import com.example.game_zone.Entities.GamesEntities.TicTacToeState;
import com.example.game_zone.Services.GameSessionService;
import com.example.game_zone.Services.GamesServices.TicTacToeService;
import com.example.game_zone.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
// this class is used to handle the tic tac toe game requests
public class TicTacToeController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TicTacToeService ticTacToeService;

    @Autowired
    private GameSessionService gameSessionService;
    //websocket endpoint to start a game
    @MessageMapping("/tic-tac-toe/start")
    public void startGame(@Payload StartGameRequest request) {
        Long sessionId = request.getSessionId();
        Optional<TicTacToeState> optionalState = ticTacToeService.getStateBySessionId(sessionId);
        optionalState.ifPresent(state -> {
            TicTacToeResponseDto responseDto = ticTacToeService.toDto(state);
            messagingTemplate.convertAndSend("/topic/game/" + sessionId, responseDto);
        });
    }
    //websocket endpoint to make a move
    @MessageMapping("/tic-tac-toe/move")
    public void handleMove(@Payload TicTacToeMoveRequest request) {
        Long sessionId = request.getSessionId();
        Long playerId = request.getPlayerId();
        int cellIndex = request.getCellIndex();

        int row = cellIndex / 3;
        int col = cellIndex % 3;

        TicTacToeResponseDto responseDto = ticTacToeService.makeMove(sessionId, playerId, row, col);
        messagingTemplate.convertAndSend("/topic/game/" + sessionId, responseDto);
    }
    //websocket endpoint to reset the game
    @MessageMapping("/tic-tac-toe/disconnect")
    public void handleDisconnect(@Payload DisconnectRequest request) {
        System.out.println("🔌 disconnect request from client");
        Long playerId = request.getPlayerId();
        Long sessionId = request.getSessionId();

        if (sessionId != null) {
            gameSessionService.handlePlayerDisconnect(playerId, sessionId, "Tic-Tac-Toe");
        }
    }
}
