package com.example.game_zone.Controllers;

import com.example.game_zone.Services.AuthService;
import com.example.game_zone.dto.RegUserDto;
import com.example.game_zone.dto.UserDto;
import com.example.game_zone.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
// this class is used to handle the authentication requests suc as login and register
public class AuthController {

    @Autowired
    private AuthService authService;
    //rest endpoint for register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            User newUser = authService.register(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
            RegUserDto response = new RegUserDto(
                    newUser.getUserId(),
                    newUser.getUsername(),
                    "User registered successfully"
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //rest endpoint for login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userdto)
    {
        Optional<User> response = authService.authenticate(userdto.getUsername(), userdto.getPassword());
        if (response.isPresent()) {
            Long userId = response.get().getUserId();
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Login successful");
            result.put("userId", userId);
            result.put("username", response.get().getUsername());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(null);
    }


}
