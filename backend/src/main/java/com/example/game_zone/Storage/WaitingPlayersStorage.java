package com.example.game_zone.Storage;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
// This class is used to store the waiting players in a concurrent hash map.
public class WaitingPlayersStorage {
    private final ConcurrentHashMap<String, Long> waitingPlayers = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Long> getWaitingPlayers() {
        return waitingPlayers;
    }

}
