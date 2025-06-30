package com.example.game_zone.Repositories;

import com.example.game_zone.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// This interface extends JpaRepository to provide DB operations for the User entity
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(Long userId);

}