package com.example.game_zone.Repositories;

import com.example.game_zone.Entities.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// This interface extends JpaRepository to provide DB operations for the GameSession entity
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    default Optional<GameSession> findByPlayer1_UserIdAndPlayer2_UserId(Long player1Id, Long player2Id) {
        return null;
    }
}