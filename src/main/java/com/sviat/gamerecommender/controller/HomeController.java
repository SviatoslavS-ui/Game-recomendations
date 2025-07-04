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
        
        // Debug: Check total games in a database
        int totalGames = gameDatabase.getAllGames().size();
        System.out.println("DEBUG: Total games in database: " + totalGames);
        
        // Best Rated Games (top 8)
        List<Game> bestRatedGames = recommendationEngine.getRecommendationsByMetacriticScore(8);
        System.out.println("DEBUG: Best rated games count: " + bestRatedGames.size());
        
        // New Releases (simulate by getting games with high review counts)
        List<Game> newReleases = gameDatabase.getAllGames().stream()
                .sorted((g1, g2) -> Integer.compare(g2.getReviewCount(), g1.getReviewCount()))
                .limit(8)
                .toList();
        System.out.println("DEBUG: New releases count: " + newReleases.size());
        
        // Recommended for You (by popular genres)
        Set<String> popularGenres = Set.of("Action", "RPG", "Adventure");
        List<Game> recommendedGames = recommendationEngine.getRecommendationsByGenre(popularGenres, 8);
        System.out.println("DEBUG: Recommended games count: " + recommendedGames.size());
        
        // Add data to the model for Thymeleaf template
        model.addAttribute("bestRatedGames", bestRatedGames);
        model.addAttribute("newReleases", newReleases);
        model.addAttribute("recommendedGames", recommendedGames);
        model.addAttribute("totalGames", totalGames);
        
        return "pages/dashboard";
    }
}
