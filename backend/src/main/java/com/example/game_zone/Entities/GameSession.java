package com.example.game_zone.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_sessions")
// this class represents a game session entity
public class GameSession {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;
    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(
            name = "player1_id", referencedColumnName = "userId", nullable = false
    )
    private User player1;


    @ManyToOne
    @JoinColumn(name = "player2_id", referencedColumnName = "userId", nullable = true)
    private User player2;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date startTime = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "winner_id", referencedColumnName = "userId")
    private User winner;

    // Constructor
    public GameSession() {}

    //Getters and Setters
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

        public User getPlayer1() {
            return player1;
        }

        public void setPlayer1(User player1) {
            this.player1 = player1;
        }

        public User getPlayer2() {
            return player2;
        }

        public void setPlayer2(User player2) {
            this.player2 = player2;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public User getWinner() {
            return winner;
        }

        public void setWinner(User winner) {
            this.winner = winner;
        }


}
