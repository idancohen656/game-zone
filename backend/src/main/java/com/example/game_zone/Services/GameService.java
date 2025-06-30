package com.example.game_zone.Services;

import com.example.game_zone.Entities.Game;
import com.example.game_zone.Entities.GameSession;
import com.example.game_zone.Entities.GamesEntities.FourInRowState;
import com.example.game_zone.Entities.GamesEntities.TicTacToeState;
import com.example.game_zone.Entities.User;
import com.example.game_zone.Repositories.GameRepository;
import com.example.game_zone.Repositories.GameSessionRepository;
import com.example.game_zone.Repositories.GamesRepositories.FourInRowRepository;
import com.example.game_zone.Repositories.GamesRepositories.TicTacToeStateRepository;
import com.example.game_zone.Repositories.UserRepository;
import com.example.game_zone.dto.OpponentResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
// This class provides services for managing games, including creating, deleting, and finding opponents.
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository sessionRepository;
    private final TicTacToeStateRepository stateRepository;
    private final FourInRowRepository fourInRowRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, Long> waitingPlayers = new HashMap<>();

    private final Map<Long, Long> activeSessions = new HashMap<>();

    @Autowired
    public GameService(GameRepository gameRepository,
                       UserRepository userRepository,
                       GameSessionRepository sessionRepository,
                       TicTacToeStateRepository stateRepository,
                       FourInRowRepository fourInRowRepository, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.stateRepository = stateRepository;
        this.fourInRowRepository = fourInRowRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }
    @Transactional
    // this method is used to find an opponent for a player in a specific game
    public synchronized OpponentResponseDto findOpponent(Long playerId, String gameName) {
        // Check if the player is already in an active session
        if (activeSessions.containsKey(playerId)) {
            Long sessionId = activeSessions.get(playerId);
            Optional<GameSession> optionalSession = sessionRepository.findById(sessionId);
            // Check if the session exists
            if (optionalSession.isPresent()) {
                GameSession session = optionalSession.get();
                // Check if the session has already ended
                if (session.getEndTime() != null) {
                    logger.info("Session {} for player {} has already ended. Clearing active session.", sessionId, playerId);
                    activeSessions.remove(playerId);
                    // If the session is still active, or it is new one, return the opponent's ID and session ID
                } else {
                    Long opponentId = session.getPlayer1().getUserId().equals(playerId)
                            ? session.getPlayer2().getUserId() : session.getPlayer1().getUserId();
                    logger.info("Player {} already has active session {} in game {}", playerId, session.getId(), gameName);
                    return new OpponentResponseDto(opponentId, session.getId(),
                            session.getPlayer1().getUsername(),
                            session.getPlayer2().getUsername());
                }
                // If the session has ended, remove it from active sessions
            } else {
                logger.info("Session {} not found in DB for player {}. Clearing active session.", sessionId, playerId);
                activeSessions.remove(playerId);
            }
        }
        logger.info("Player {} is looking for an opponent in game {}", playerId, gameName);
        // Check if there is a waiting player for the same game
        Long waitingPlayerId = waitingPlayers.get(gameName);
        // If there is no waiting player or the waiting player is the same as the current player we add him back to the waiting list
        if (waitingPlayerId == null || waitingPlayerId.equals(playerId)) {
            waitingPlayers.put(gameName, playerId);
            logger.info("No waiting player found. Player {} added to waiting list for game {}", playerId, gameName);
            return null;
        }
        // If there is a waiting player, create a new game session for both players
        else {
            waitingPlayers.remove(gameName, waitingPlayerId);
            logger.info("Found waiting player {} for player {} in game {}", waitingPlayerId, playerId, gameName);
            GameSession session = createNewGame(waitingPlayerId, playerId, gameName);
            logger.info("Created game session {} for players {} and {}", session.getId(), waitingPlayerId, playerId);
            activeSessions.put(waitingPlayerId, session.getId());
            activeSessions.put(playerId, session.getId());
            messagingTemplate.convertAndSendToUser(waitingPlayerId.toString(), "/queue/session", session); // send session info to waiting player
            return new OpponentResponseDto(waitingPlayerId, session.getId(),
                    session.getPlayer1().getUsername(),
                    session.getPlayer2().getUsername());
        }
    }

    // this method is used to create a new game session for two players
    @Transactional
    public GameSession createNewGame(Long player1Id, Long player2Id, String gameName) {
        User player1 = userRepository.findById(player1Id)
                .orElseThrow(() -> new IllegalArgumentException("Player 1 not found"));
        User player2 = userRepository.findById(player2Id)
                .orElseThrow(() -> new IllegalArgumentException("Player 2 not found"));

        Game game = gameRepository.findByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        GameSession session = new GameSession();
        session.setPlayer1(player1);
        session.setPlayer2(player2);
        session.setGame(game);
        session = sessionRepository.save(session);

        switch(gameName) {
            case "Tic-Tac-Toe": {
                TicTacToeState state = new TicTacToeState(session, player1.getUserId(), player2.getUserId(), player1.getUserId());
                stateRepository.save(state);
                break;
            }
            case "Checkers": {
                // TODO: Initialize Checkers game state
                break;
            }
            case "4 in a Row": {
                FourInRowState state = new FourInRowState(session, player1.getUserId(), player2.getUserId(), player1.getUserId());
                fourInRowRepository.save(state);
                break;
            }
            case "War": {
                // TODO: Initialize War game state
                break;
            }
            case "Memory": {
                // TODO: Initialize Memory game state
                break;
            }
            case "RockPaperScissors": {
                // TODO: Initialize RockPaperScissors game state
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported game: " + gameName);
        }
        return session;
    }
    // this method is used to remove a player from the waiting list
    public void clearActiveSession(Long sessionId) {
        List<Long> playersToRemove = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : activeSessions.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                playersToRemove.add(entry.getKey());
            }
        }
        for (Long playerId : playersToRemove) {
            activeSessions.remove(playerId);
        }
    }


}
