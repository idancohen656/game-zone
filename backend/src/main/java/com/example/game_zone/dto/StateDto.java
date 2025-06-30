package com.example.game_zone.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlRootElement(name = "playerStats")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
// DTO class to hold player statistics in XML format
public class StateDto {

    @XmlElement
    private Long player1Id;

    @XmlElement
    private Long gameWins;

    @XmlElement
    private Long totalWins;

    @XmlElement
    private Long losses;
    @XmlElement
    private Long draws;

    public StateDto() {}

    public StateDto(Long player1Id, Long gameWins, Long totalWins, Long losses, Long draws) {
        this.player1Id = player1Id;
        this.gameWins = gameWins;
        this.totalWins = totalWins;
        this.losses = losses;
        this.draws = draws;
    }
}
