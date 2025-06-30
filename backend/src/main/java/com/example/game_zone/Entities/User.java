package com.example.game_zone.Entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
// this class represents a user entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    // Constructor
    public User() {}

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long id) { this.userId = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getCreatedAt() { return createdAt; }

    public boolean isPresent()
    {
        return false;
    }
}
