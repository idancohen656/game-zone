package com.example.game_zone.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
// this class represents a statistics entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private Game game;
    @Column(nullable = false)
    private int duration;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winner_id", referencedColumnName = "userId")
    private User winner;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_session_id", referencedColumnName = "session_id")
    private GameSession gameSession;

    // Constructor
    public Statistics() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public User getWinner() {
        return winner;
    }
    public void setWinner(User winner) {
        this.winner = winner;
    }

    public GameSession getGameSession() {return gameSession;}
    public void setGameSession(GameSession gameSession) {this.gameSession = gameSession;}
}
