package com.sviat.gamerecommender.controller;

import com.sviat.gamerecommender.dto.RecommendationRequest;
import com.sviat.gamerecommender.model.Game;
import com.sviat.gamerecommender.service.RecommendationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // 3. If no filters are applied, return empty response
        if (genres.isEmpty() && tags.isEmpty()) {
            // Add empty list for backward compatibility
            model.addAttribute("recommendedGames", Collections.emptyList());
            return "fragments/recommendation-results :: recommendationResults";
        }

        // 4. Apply multi-filter recommendation logic
        List<Game> recommendedGames = recommendationEngine.getMultiFilterRecommendations(
                genres, tags, 15);

        // 5. Categorize games by match score using the existing calculateMatchScore
        // method
        Map<String, List<Game>> categorizedGames = categorizeGamesByScore(
                recommendedGames, genres, tags);

        // 6. Add categorized games to the model
        model.addAttribute("perfectMatches", categorizedGames.get("perfectMatches"));
        model.addAttribute("goodMatches", categorizedGames.get("goodMatches"));
        model.addAttribute("otherMatches", categorizedGames.get("otherMatches"));

        // For backward compatibility
        model.addAttribute("recommendedGames", recommendedGames);

        // Return only the fragment with game cards
        return "fragments/recommendation-results :: recommendationResults";
    }

    /**
     * Categorizes games into different sections based on their match scores.
     * Uses the RecommendationEngine's calculateMatchScore method to evaluate each
     * game.
     * 
     * @param games  List of games to categorize
     * @param genres Set of genres to match against
     * @param tags   Set of tags to match against
     * @return Map containing categorized game lists
     */
    private Map<String, List<Game>> categorizeGamesByScore(List<Game> games, Set<String> genres, Set<String> tags) {
        Map<String, List<Game>> result = new HashMap<>();

        List<Game> perfectMatches = new ArrayList<>();
        List<Game> goodMatches = new ArrayList<>();
        List<Game> otherMatches = new ArrayList<>();

        // Define score thresholds based on our scoring algorithm
        final int PERFECT_MATCH_THRESHOLD = 240; // 2/2 genres (200) + partial tag match (40) = 240
        final int GOOD_MATCH_THRESHOLD = 120; // 2/3 genres (80) + partial tag match (40) = 120

        // Categorize by match score for all games
        for (Game game : games) {
            int score = recommendationEngine.calculateMatchScore(game, genres, tags);

            int category = score >= PERFECT_MATCH_THRESHOLD ? 1 : score >= GOOD_MATCH_THRESHOLD ? 2 : 3;

            switch (category) {
                case 1 -> perfectMatches.add(game);
                case 2 -> goodMatches.add(game);
                case 3 -> otherMatches.add(game);
            }
        }

        result.put("perfectMatches", perfectMatches);
        result.put("goodMatches", goodMatches);
        result.put("otherMatches", otherMatches);

        return result;
    }

    /**
     * Get 3 similar games based on genre and tag filters
     * 
     * @param game the game to find related games for
     * @return list of related games
     */
    @GetMapping("/{gameId}/related")
    @ResponseBody
    public List<Map<String, String>> getRelatedGames(@PathVariable String gameId) {
        return recommendationEngine.getRelatedGames(gameId, 3);
    }
}
