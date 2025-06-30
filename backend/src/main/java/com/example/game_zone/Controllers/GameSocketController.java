package com.example.game_zone.Controllers;

import com.example.game_zone.Entities.GameSession;
import com.example.game_zone.Services.GameService;
import com.example.game_zone.Services.GameSessionService;
import com.example.game_zone.Storage.WaitingPlayersStorage;
import com.example.game_zone.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Controller
// This class is used to handle the game socket requests
public class GameSocketController {

    @Autowired
    private WaitingPlayersStorage waitingPlayersStorage;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameService gameService;

    //websocket endpoint to find an opponent
    @MessageMapping("/joinQueue")
    public void joinQueue(GameRequestDto request) {
        Long playerId = request.getPlayerId();
        String gameName = request.getGameName();
        waitingPlayersStorage.getWaitingPlayers().put(gameName, playerId);
        System.out.println("Player " + playerId + " is waiting for opponent in " + gameName);
    }
    //websocket endpoint to leave a game queue
    @MessageMapping("/createSession")
    public synchronized void createSession(GameRequestDto request) {
        Long playerId = request.getPlayerId();
        String gameName = request.getGameName();

        Long waitingPlayerId = waitingPlayersStorage.getWaitingPlayers().remove(gameName);

        if (waitingPlayerId != null && !waitingPlayerId.equals(playerId)) {
            System.out.println("Match created: " + waitingPlayerId + " vs " + playerId);

            GameSession session = gameService.createNewGame(waitingPlayerId, playerId, gameName);
            GameSessionDto sessionDto = new GameSessionDto(
                    session.getPlayer1().getUserId(),
                    session.getPlayer2().getUserId(),
                    session.getPlayer1().getUsername(),
                    session.getPlayer2().getUsername(),
                    session.getId()
            );

            messagingTemplate.convertAndSend("/topic/foundOpponent-" + waitingPlayerId, sessionDto);
            messagingTemplate.convertAndSend("/topic/foundOpponent-" + playerId, sessionDto);
        } else {
            System.out.println("No waiting player found for " + gameName);
        }
    }


}
