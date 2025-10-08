package com.sviat.gamerecommender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sviat.gamerecommender.service.GameDatabase;
import com.sviat.gamerecommender.service.JsonService;
import com.sviat.gamerecommender.service.RecommendationEngine;

@Configuration
public class GameConfig {
    
    @Value("${game.data.path:data/games.json}")
    private String gameDataPath;
    
    @Bean
    public JsonService jsonService() {
        return new JsonService();
    }
    
    @Bean
    public GameDatabase gameDatabase(JsonService jsonService) {
        GameDatabase database = new GameDatabase(jsonService);
        // Load games from the configured path
        database.loadGamesFromFile(gameDataPath);
        System.out.println("GameDatabase initialized with " + database.getAllGames().size() + " games");
        return database;
    }
    
    @Bean
    public RecommendationEngine recommendationEngine(GameDatabase gameDatabase) {
        return new RecommendationEngine(gameDatabase);
    }
}
