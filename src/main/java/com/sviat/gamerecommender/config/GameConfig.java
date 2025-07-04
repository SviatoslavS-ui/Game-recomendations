package com.sviat.gamerecommender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sviat.gamerecommender.service.GameDatabase;
import com.sviat.gamerecommender.service.JsonService;
import com.sviat.gamerecommender.service.RecommendationEngine;

@Configuration
public class GameConfig {
    
    @Bean
    public JsonService jsonService() {
        return new JsonService();
    }
    
    @Bean
    public GameDatabase gameDatabase(JsonService jsonService) {
        GameDatabase database = new GameDatabase(jsonService);
        // read games from the games.json file
        database.loadGamesFromFile("src/main/resources/data/games.json");
        return database;
    }
    
    @Bean
    public RecommendationEngine recommendationEngine(GameDatabase gameDatabase) {
        return new RecommendationEngine(gameDatabase);
    }
}
