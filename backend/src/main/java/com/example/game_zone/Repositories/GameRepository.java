package com.example.game_zone.Repositories;

import com.example.game_zone.Entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// This interface extends JpaRepository to provide DB operations for the Game entity
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);
}

