package com.example.game_zone.Entities;
import jakarta.persistence.*;

@Entity
@Table(name = "games")
// this class represents a game entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long game_id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rules;

    // Constructor
    public Game() {}

    // Getters and Setters
    public Long getId() { return game_id; }
    public void setId(Long id) { this.game_id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }
}
