package com.example.game_zone.dto;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "leaderboard")
// dto class to hold leaderboard data in xml format
public class LeaderBoardDto {

    @XmlElement(name = "entry")
    private List<LeaderBoardEntry> entries;

    public LeaderBoardDto() {}

    public LeaderBoardDto(List<LeaderBoardEntry> entries) {
        this.entries = entries;
    }
}
