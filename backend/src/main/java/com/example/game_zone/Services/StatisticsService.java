package com.example.game_zone.Services;

import com.example.game_zone.Repositories.StatisticsRepository;
import com.example.game_zone.Repositories.UserRepository;
import com.example.game_zone.dto.LeaderBoardDto;
import com.example.game_zone.dto.LeaderBoardEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
// This class is responsible for handling statistics operations
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private UserRepository userRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository, UserRepository userRepository) {
        this.statisticsRepository = statisticsRepository;
        this.userRepository = userRepository;
    }

    public int getGameWins(Long userId, Long gameId) {
        return statisticsRepository.countGameWins(userId, gameId);
    }
    public int getTotalWins(Long userId) {
        return statisticsRepository.countTotalWins(userId);
    }
    public int getLosses(Long userId) {
        return statisticsRepository.countLosses(userId);
    }
    public int getDraws(Long userId) {
        return statisticsRepository.countDraws(userId);
    }
    public LeaderBoardDto getLeaderboardForGame(Long gameId) {
        List<Object[]> rawData = statisticsRepository.getTopWinnersByGame(gameId);
        //
        List<LeaderBoardEntry> entries = rawData.stream()
                .map(obj -> new LeaderBoardEntry((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        return new LeaderBoardDto(entries);
    }

}
