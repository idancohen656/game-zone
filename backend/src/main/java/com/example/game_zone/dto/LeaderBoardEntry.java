package com.example.game_zone.dto;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
// this class is used to hold leaderboard entry data in xml format
public class LeaderBoardEntry {

    @XmlElement
    private String username;

    @XmlElement
    private Long wins;

    public LeaderBoardEntry() {}

    public LeaderBoardEntry(String username, Long wins) {
        this.username = username;
        this.wins = wins;
    }
}

