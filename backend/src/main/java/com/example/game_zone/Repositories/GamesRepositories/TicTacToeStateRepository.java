package com.example.game_zone.Repositories.GamesRepositories;

import com.example.game_zone.Entities.GamesEntities.TicTacToeState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// This interface extends JpaRepository to provide DB operations for the TicTacToeState entity
public interface TicTacToeStateRepository extends JpaRepository<TicTacToeState, Long> {
    Optional<TicTacToeState> findBySessionId(Long sessionId);

}
