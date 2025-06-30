package com.example.game_zone.Controllers;

import com.example.game_zone.Services.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameSession")
// This class is used to handle the game session requests
public class GameSessionController {
    private final GameSessionService gameSessionService;
    // Constructor-based dependency injection
    @Autowired
    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }
    //rest endpoint to get game session by id
    @GetMapping ("/{session_id}")
    public String getGameSessionById(Long session_id) {
        return gameSessionService.getGameSessionById(session_id);
    }
    //rest endpoint to create game session
    @PostMapping ("/create")
    public void createGameSession() {
        gameSessionService.createGameSession();
    }
    //rest endpoint to get all game sessions
    @GetMapping ("/all")
    public String getGameSession() {
        return gameSessionService.getGameSession();
    }
}
