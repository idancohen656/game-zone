package com.example.game_zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.game_zone.Entities")

public class GameZoneApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(GameZoneApplication.class, args);
    }

}
