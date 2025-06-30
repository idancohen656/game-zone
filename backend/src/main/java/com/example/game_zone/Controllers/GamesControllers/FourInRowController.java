package com.example.game_zone.Controllers.GamesControllers;

import com.example.game_zone.Entities.GamesEntities.FourInRowState;
import com.example.game_zone.Services.GameSessionService;
import com.example.game_zone.Services.GamesServices.FourInRowService;
import com.example.game_zone.dto.DisconnectRequest;
import com.example.game_zone.dto.FourInRowMoveReq;
import com.example.game_zone.dto.FourInRowResponseDto;
import com.example.game_zone.dto.StartGameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
// This class handles WebSocket messages for the "4 in a Row" game.
public class FourInRowController {
    @Autowired
    private FourInRowService fourInRowService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private GameSessionService gameSessionService;
    @MessageMapping("/four-in-row/start")
    // this method handles the start game request from the client.
    public void startGame(@Payload StartGameRequest request) {
        System.out.println("📥 FourInRow START: session=" + request.getSessionId() + ", player=" + request.getPlayerId());

        Long sessionId = request.getSessionId();
        Optional<FourInRowState> optionalState = fourInRowService.getStateBySessionId(sessionId);
        optionalState.ifPresent(state -> {
            FourInRowResponseDto responseDto = fourInRowService.toDto(state);
            messagingTemplate.convertAndSend("/topic/game/" + sessionId, responseDto);
        });
    }
    // this method handles the move request from the client.
    @MessageMapping("/four-in-row/move")
    public void handleMove(@Payload FourInRowMoveReq request) {
        Long sessionId = request.getSessionId();
        Long playerId = request.getPlayerId();
        int column = request.getColumn();

        FourInRowResponseDto responseDto = fourInRowService.makeMove(sessionId, playerId, column);
        messagingTemplate.convertAndSend("/topic/game/" + sessionId, responseDto);
    }
    // this method handles the disconnect request from the client.
    @MessageMapping("/four-in-row/disconnect")
    public void handleDisconnect(@Payload DisconnectRequest request) {
        System.out.println("🔌 disconnect request from client");
        Long playerId = request.getPlayerId();
        Long sessionId = request.getSessionId();

        if (sessionId != null) {
            gameSessionService.handlePlayerDisconnect(playerId, sessionId, "4 in a Row");
        }
    }
}
