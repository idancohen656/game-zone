package com.example.game_zone.Entities.GamesEntities;

import com.example.game_zone.Entities.GameSession;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "four_in_row_states")
// This class represents the state of a Four in a Row game
public class FourInRowState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    private GameSession session; // Reference to the game session

    @Column(nullable = false, length = 42)
    private String board = "------------------------------------------"; // 42 characters representing 6*7 board state (X, O, or - for empty)

    @Column(name = "current_turn", nullable = false)
    private Long currentTurn; // ID of the player whose turn it is

    @Column(name = "player1_id", nullable = false)
    private Long player1Id;  // ID of player 1

    @Column(name = "player2_id", nullable = false)
    private Long player2Id; // ID of player 2

    @Column(name = "status", nullable = false)
    private String status = "IN_PROGRESS"; // IN_PROGRESS / FINISHED / DRAW

    @ElementCollection(fetch = FetchType.EAGER) // EAGER fetch type to load winning cells immediately
    @CollectionTable(name = "four_in_row_winning_cells", joinColumns = @JoinColumn(name = "state_id"))
    @Column(name = "cell_index")
    private List<Integer> winningCells = new ArrayList<>();// collection of winning cells
    // Constructors
    public FourInRowState() {
    }

    public FourInRowState(GameSession session, Long player1Id, Long player2Id, Long currentTurn) {
        this.session = session;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.currentTurn = currentTurn;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Long getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Long currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getWinningCells() {
        return winningCells;
    }

    public void setWinningCells(List<Integer> winningCells) {
        this.winningCells = winningCells;
    }
}
