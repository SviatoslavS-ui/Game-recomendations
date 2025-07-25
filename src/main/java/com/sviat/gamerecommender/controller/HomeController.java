package com.sviat.gamerecommender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sviat.gamerecommender.service.GameDatabase;
import com.sviat.gamerecommender.service.RecommendationEngine;
import com.sviat.gamerecommender.model.Game;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    
    private final GameDatabase gameDatabase;
    private final RecommendationEngine recommendationEngine;
    
    @Autowired
    public HomeController(GameDatabase gameDatabase, RecommendationEngine recommendationEngine) {
        this.gameDatabase = gameDatabase;
        this.recommendationEngine = recommendationEngine;
    }
    
    @GetMapping("/")
    public String dashboard(Model model) {
        // Get different game categories for the dashboard        
        int totalGames = gameDatabase.getAllGames().size();
        
        // Best Rated Games (top 12)
        List<Game> bestRatedGames = recommendationEngine.getRecommendationsByMetacriticScore(12);
        
        // New Releases (top 12)
        List<Game> newReleases = recommendationEngine.getRecommendationsByReleaseDate(12);
        
        // Recommended for You (by popular genres - Action, RPG, Adventure)
        Set<String> popularGenres = Set.of("Action", "RPG", "Adventure");
        List<Game> recommendedGames = recommendationEngine.getRecommendationsByGenre(popularGenres, 12);
        
        // Add data to the model for Thymeleaf template
        model.addAttribute("bestRatedGames", bestRatedGames);
        model.addAttribute("newReleases", newReleases);
        model.addAttribute("recommendedGames", recommendedGames);
        model.addAttribute("totalGames", totalGames);
        
        return "pages/dashboard";
    }
}
