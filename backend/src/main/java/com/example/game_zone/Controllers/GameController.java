package com.example.game_zone.Controllers;

import com.example.game_zone.Entities.Game;
import com.example.game_zone.Services.GameService;
import com.example.game_zone.Storage.WaitingPlayersStorage;
import com.example.game_zone.dto.OpponentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/games")
// This class is used to handle the game requests such as create game, get game by id, find opponent
public class GameController {

    private final GameService gameService;
    private final ConcurrentHashMap<String, Long> waitingPlayers;


    private WaitingPlayersStorage waitingPlayersStorage;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
        this.waitingPlayers = new ConcurrentHashMap<>();
    }


    public GameController(GameService gameService, ConcurrentHashMap<String, Long> waitingPlayers) {
        this.gameService = gameService;
        this.waitingPlayers = waitingPlayers;
    }

    @GetMapping
    public List<Game> getGames() {
        return gameService.getGames();
    }
    //rest api to create a new game
    @PostMapping("/create")
    public Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    //rest api to update a game
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }
    //rest api to update a game
    @PostMapping("/findOpponent")
    public OpponentResponseDto findOpponent(@RequestParam Long playerId, @RequestParam String gameName) {
        return gameService.findOpponent(playerId, gameName);
    }
    //rest api to update a game
    @GetMapping("/api/game/waiting/{gameName}")
    public Map<String, Boolean> isWaiting(@PathVariable String gameName) {
        boolean waiting = waitingPlayersStorage.getWaitingPlayers().containsKey(gameName);
        return Collections.singletonMap("waiting", waiting);
    }

}
