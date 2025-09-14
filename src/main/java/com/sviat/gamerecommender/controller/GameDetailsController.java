package com.sviat.gamerecommender.controller;

import com.sviat.gamerecommender.model.Game;
import com.sviat.gamerecommender.service.GameDatabase;
import com.sviat.gamerecommender.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling game details requests
 */
@Controller
@RequestMapping("/api/games")
public class GameDetailsController {

    private final GameDatabase gameDatabase;
    private final S3Service s3Service;

    @Autowired
    public GameDetailsController(GameDatabase gameDatabase, S3Service s3Service) {
        this.gameDatabase = gameDatabase;
        this.s3Service = s3Service;
    }

    /**
     * Get game details including HTML content from S3
     * 
     * @param gameId the ID of the game
     * @return JSON response with game data and HTML content
     */
    @GetMapping("/{gameId}/details")
    @ResponseBody
    public ResponseEntity<?> getGameDetails(@PathVariable String gameId) {
        // Find the game by ID
        Game game = gameDatabase.findGameById(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        // Create response with game data and HTML content
        Map<String, Object> response = new HashMap<>();
        response.put("id", game.getId());
        response.put("title", game.getTitle());
        response.put("description", game.getDescription());
        response.put("imageUrl", game.getImageUrl()); // Original image URL from S3        
        response.put("developer", game.getDeveloper());
        response.put("publisher", game.getPublisher());
        response.put("genres", game.getGenres());
        response.put("tags", game.getTags());
        response.put("metacriticScore", game.getMetacriticScore());
        response.put("userScore", game.getUserScore());
        response.put("platforms", game.getPlatforms());
        response.put("price", game.getPrice());
        response.put("isMultiplayer", game.isMultiplayer());
        response.put("playtimeHours", game.getPlaytimeHours());
        response.put("ageRating", game.getAgeRating());
        response.put("detailsHtml", s3Service.getGameDetails(gameId));
        response.put("releaseDate", game.getReleaseDate());
        
        return ResponseEntity.ok(response);
    }    
}
