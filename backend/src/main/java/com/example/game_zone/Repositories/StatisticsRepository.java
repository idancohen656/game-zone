package com.example.game_zone.Repositories;

import com.example.game_zone.Entities.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// This interface extends JpaRepository to provide DB operations for the Statistics entity
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    // this query counts the wins for a specific user in a specific game
    @Query("SELECT COUNT(s) FROM GameSession s WHERE s.winner.userId = :userId AND s.game.game_id= :gameId")
    int countGameWins(@Param("userId") Long userId, @Param("gameId") Long gameId);
    // this query counts the draws
    @Query("SELECT COUNT (s) FROM GameSession s WHERE s.player1.userId = :userId OR s.player2.userId = :userId AND s.winner IS NULL AND s.endTime IS NOT NULL")
    int countDraws(@Param("userId") Long userId);
    // this query counts the total wins
    @Query("SELECT COUNT(s) FROM GameSession s WHERE s.winner.userId = :userId")
    int countTotalWins(@Param("userId") Long userId);
    // this query counts the total losses
    @Query("""
    SELECT COUNT(s) 
    FROM GameSession s 
    WHERE 
        (s.player1.userId = :userId OR s.player2.userId = :userId)
        AND s.winner IS NOT NULL 
        AND s.winner.userId <> :userId
        
""")
    int countLosses(@Param("userId") Long userId);
    // this query creates a leaderboard for a specific game
    @Query("""
    SELECT s.winner.username, COUNT(s) 
    FROM GameSession s 
    WHERE s.winner IS NOT NULL AND s.game.game_id = :gameId 
    GROUP BY s.winner.username 
    ORDER BY COUNT(s) DESC
""")
    List<Object[]> getTopWinnersByGame(@Param("gameId") Long gameId);

}
