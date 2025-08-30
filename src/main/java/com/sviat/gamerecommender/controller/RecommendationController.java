package com.sviat.gamerecommender.controller;

import com.sviat.gamerecommender.dto.RecommendationRequest;
import com.sviat.gamerecommender.model.Game;
import com.sviat.gamerecommender.service.RecommendationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationEngine recommendationEngine;

    @Autowired
    public RecommendationController(RecommendationEngine recommendationEngine) {
        this.recommendationEngine = recommendationEngine;
    }

    @GetMapping
    public String getRecommendationsPage() {
        return "pages/recommendations";
    }

    @PostMapping("/results")
    public String getRecommendations(@RequestBody RecommendationRequest request, Model model) {
        
        // 1. Get genres from request (if any)
        Set<String> genres = request.getGenresAsSet();
        
        // 2. Get tags from request (if any)
        Set<String> tags = request.getTagsAsSet();
        
        // 3. Apply multi-filter recommendation logic
        List<Game> recommendedGames = recommendationEngine.getMultiFilterRecommendations(
                genres,
                tags,
                10 // Limit to 20 games
        );
        
        model.addAttribute("recommendedGames", recommendedGames);
        
        // Return only the fragment with game cards
        return "fragments/recommendation-results :: recommendationResults";
    }
}
