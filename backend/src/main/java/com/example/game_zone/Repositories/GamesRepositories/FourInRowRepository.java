package com.example.game_zone.Repositories.GamesRepositories;

import com.example.game_zone.Entities.GamesEntities.FourInRowState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// This interface extends JpaRepository to provide DB operations for the FourInRowState entity
public interface FourInRowRepository extends JpaRepository<FourInRowState, Long> {
    Optional<FourInRowState> findBySessionId(Long sessionId);
}
