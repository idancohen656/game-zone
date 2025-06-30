package com.example.game_zone.Services;

import com.example.game_zone.Entities.GameSession;
import com.example.game_zone.Entities.GamesEntities.FourInRowState;
import com.example.game_zone.Entities.GamesEntities.TicTacToeState;
import com.example.game_zone.Entities.User;
import com.example.game_zone.Repositories.GameSessionRepository;
import com.example.game_zone.Repositories.GamesRepositories.FourInRowRepository;
import com.example.game_zone.Repositories.GamesRepositories.TicTacToeStateRepository;
import com.example.game_zone.Repositories.UserRepository;
import com.example.game_zone.dto.FourInRowResponseDto;
import com.example.game_zone.dto.TicTacToeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
// this class is responsible for managing game sessions
public class GameSessionService {

    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final TicTacToeStateRepository ticTacToeStateRepository;
    private final FourInRowRepository fourInRowRepository;
    private final Map<Long, Long> activeSessions = new ConcurrentHashMap<>();
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository,
                              TicTacToeStateRepository ticTacToeStateRepository, FourInRowRepository fourInRowRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
        this.ticTacToeStateRepository = ticTacToeStateRepository;
        this.fourInRowRepository = fourInRowRepository;
    }

    public String getGameSessionById(Long session_id) {
        return gameSessionRepository.findById(session_id).orElse(null).toString();
    }

    public void createGameSession() {
        gameSessionRepository.save(null);
    }

    public String getGameSession() {
        return gameSessionRepository.findAll().toString();
    }

    // this method is used to create a new game session
    public void handlePlayerDisconnect(Long playerId, Long sessionId, String gameName) {
        Optional<GameSession> optionalSession = gameSessionRepository.findById(sessionId);
        if (optionalSession.isEmpty()) return; // session not found
        GameSession session = optionalSession.get();
        if (session.getEndTime() != null) return; // already ended
        // check if the player is part of the session
        Long winnerId = session.getPlayer1().getUserId().equals(playerId)
                ? session.getPlayer2().getUserId()
                : session.getPlayer1().getUserId();
        //
        Optional<User> winnerOpt = userRepository.findById(winnerId);
        // check if the winner is found
        if (winnerOpt.isEmpty()) return;
        User winner = winnerOpt.get();
        // set the winner and end time
        session.setWinner(winner);
        session.setEndTime(new Date());
        gameSessionRepository.save(session);

        switch (gameName) {
            // handle the game state based on the game name
            case "Tic-Tac-Toe" -> {
                // find the game state by session ID
                Optional<TicTacToeState> gameStateOpt = ticTacToeStateRepository.findBySessionId(sessionId);
                // check if the game state is found and update the status and winning cells
                gameStateOpt.ifPresent(state -> {
                    state.setStatus("FINISHED");
                    state.setWinningCells(new ArrayList<>());
                    ticTacToeStateRepository.save(state);
                    // send the game state to the players
                    messagingTemplate.convertAndSend("/topic/game/" + sessionId,
                            new TicTacToeResponseDto(
                                    state.getBoard().split(""),
                                    null,
                                    winner.getUsername(),
                                    new ArrayList<>(),
                                    "FINISHED"
                            )
                    );
                });
            }
            case "4 in a Row" -> {
                // find the game state by session ID
                Optional<FourInRowState> gameStateOpt = fourInRowRepository.findBySessionId(sessionId);
                // check if the game state is found and update the status and winning cells
                gameStateOpt.ifPresent(state -> {
                    state.setStatus("FINISHED");
                    fourInRowRepository.save(state);
                    // send the game state to the players
                    messagingTemplate.convertAndSend("/topic/game/" + sessionId,
                            new FourInRowResponseDto(
                                    state.getBoard().split(""),
                                    null,
                                    winner.getUsername(),
                                    new ArrayList<>(),
                                    "FINISHED"
                            )
                    );
                });
            }
            default -> System.out.println("Unsupported game type for disconnect handling: " + gameName);
        }


        // remove the session from active sessions
        activeSessions.remove(session.getPlayer1().getUserId());
        activeSessions.remove(session.getPlayer2().getUserId());

        System.out.println("Session " + sessionId + " ended. Winner: " + winner.getUsername());
    }


}
