package com.example.game_zone.Controllers;

import com.example.game_zone.Services.StatisticsService;
import com.example.game_zone.dto.LeaderBoardDto;
import com.example.game_zone.dto.StateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
// This class is used to handle the statistics requests
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_XML_VALUE)
    // This endpoint returns the summary of the statistics for a specific game
    public StateDto getSummary(
            @RequestParam Long userId,
            @RequestParam(required = false) Long gameId
    ) {
        long gameWins = (gameId != null) ? statisticsService.getGameWins(userId, gameId) : 0;

        StateDto dto = new StateDto();
        dto.setPlayer1Id(userId);
        dto.setGameWins(gameWins);
        return dto;
    }
    @GetMapping(value = "/summary/total", produces = MediaType.APPLICATION_XML_VALUE)
    // This endpoint returns the total summary of the statistics for a specific user
    public StateDto getTotalSummary(
            @RequestParam Long userId
    ) {
        long totalWins = statisticsService.getTotalWins(userId);
        long losses = statisticsService.getLosses(userId);
        long draws = statisticsService.getDraws(userId);

        StateDto dto = new StateDto();
        dto.setPlayer1Id(userId);
        dto.setTotalWins(totalWins);
        dto.setLosses(losses);
        dto.setDraws(draws);
        return dto;
    }
    @GetMapping(value = "/leaderboard", produces = MediaType.APPLICATION_XML_VALUE)
    // This endpoint returns the leaderboard for a specific game
    public LeaderBoardDto getLeaderboard(@RequestParam Long gameId) {
        return statisticsService.getLeaderboardForGame(gameId);
    }
}
