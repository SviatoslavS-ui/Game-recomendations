package com.sviat.gamerecommender.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.sviat.gamerecommender.model.Game;

public class RecommendationEngine {
    private final GameDatabase gameDatabase;

    public RecommendationEngine(GameDatabase gameDatabase) {
        this.gameDatabase = gameDatabase;
    }

    public List<Game> getRecommendationsByGenre(Set<String> genres, int limit) {
        Comparator<Game> byMatchCountDesc = Comparator.comparingLong(
                (Game g) -> g.getGenres().stream()
                        .filter(genres::contains)
                        .count())
                .reversed();

        return gameDatabase.getAllGames().stream()
                // keep only games that match at least one genre
                .filter(g -> !java.util.Collections.disjoint(g.getGenres(), genres))
                // sort by number of matching genres (desc)
                .sorted(byMatchCountDesc)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Game> getRecommendationsByMetacriticScore(int limit) {
        return gameDatabase.getAllGames().stream()
                // sort by metacritic score (desc)
                .sorted(Comparator.comparingInt(Game::getMetacriticScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Game> getRecommendationsByDeveloper(String developer, int limit) {
        return gameDatabase.getAllGames().stream()
                // keep only games by the specified developer
                .filter(g -> g.getDeveloper().equals(developer))
                // sort by metacritic score (desc)
                .sorted(Comparator.comparingInt(Game::getMetacriticScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Game> getRecommendationsByPlatform(String platform, int limit) {
        return gameDatabase.getAllGames().stream()
                // keep only games by the specified platform
                .filter(g -> g.getPlatforms().contains(platform))
                // sort by metacritic score (desc)
                .sorted(Comparator.comparingInt(Game::getMetacriticScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Game> getRecommendationsByReleaseDate(int limit) {
        // Define formatter for ISO date format (YYYY-MM-DD)
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Comparator<Game> byReleaseDateDesc = (g1, g2) -> {
            try {
                LocalDate date1 = LocalDate.parse(g1.getReleaseDate(), formatter);
                LocalDate date2 = LocalDate.parse(g2.getReleaseDate(), formatter);
                // Reversed for descending order (newest first)
                return date2.compareTo(date1);
            } catch (DateTimeParseException e) {
                return 0;
            }
        };
        
        return gameDatabase.getAllGames().stream()
                // Filter out games with null or empty release dates
                .filter(game -> game.getReleaseDate() != null && !game.getReleaseDate().isEmpty())
                // Sort by parsed release date (descending order)
                .sorted(byReleaseDateDesc)
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Multi-filter recommendation method that applies filters and sorts by match quality:
     * 1. Keep games that match at least one criterion (genre or tag)
     * 2. Calculate a match score for each game based on how many criteria it matches
     * 3. Sort by match score (descending) and then by metacritic score (descending)
     * 4. Limit to the requested number of results
     */
    public List<Game> getMultiFilterRecommendations(Set<String> genres, Set<String> tags, int limit) {
        // Prepare filters, handling null cases
        final Set<String> effectiveGenres = (genres != null) ? genres : Set.of();
        final Set<String> effectiveTags = (tags != null) ? tags : Set.of();
        
        // Get all games
        List<Game> allGames = new ArrayList<>(gameDatabase.getAllGames());
        
        // Create a comparator that sorts by match quality
        Comparator<Game> byMatchQuality = (game1, game2) -> {
            // Calculate match scores for both games
            int score1 = calculateMatchScore(game1, effectiveGenres, effectiveTags);
            int score2 = calculateMatchScore(game2, effectiveGenres, effectiveTags);
            
            // First sort by match score (descending)
            int scoreComparison = Integer.compare(score2, score1);
            if (scoreComparison != 0) {
                return scoreComparison;
            }
            
            // If match scores are equal, sort by metacritic score (descending)
            return Integer.compare(game2.getMetacriticScore(), game1.getMetacriticScore());
        };
        
        // Filter and sort games
        return allGames.stream()
                // Keep only games that match at least one criterion
                .filter(game -> {
                    boolean matchesGenre = effectiveGenres.isEmpty() || 
                            !java.util.Collections.disjoint(game.getGenres(), effectiveGenres);
                    boolean matchesTag = effectiveTags.isEmpty() || 
                            game.getTags().stream().anyMatch(effectiveTags::contains);
                    
                    // Game must match at least one criterion if filters are provided
                    return (effectiveGenres.isEmpty() && effectiveTags.isEmpty()) || 
                           matchesGenre || matchesTag;
                })
                // Sort by match quality
                .sorted(byMatchQuality)
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Constants for scoring algorithm
    private static final int PERFECT_MATCH_SCORE = 100;
    private static final int PARTIAL_MATCH_BASE_SCORE = 50;
    private static final double GENRE_WEIGHT = 1.3;  // Genres weighted more than tags
    private static final double TAG_WEIGHT = 1.0;

    /**
     * Calculates a match score for a game based on how well it matches the provided criteria.
     * The score is higher for games that match more criteria.
     * Genres are weighted slightly more than tags in the scoring algorithm.
     * 
     * @param game The game to evaluate
     * @param genres Set of genres to match
     * @param tags Set of tags to match
     * @return A score representing how well the game matches the criteria
     */
    private int calculateMatchScore(Game game, Set<String> genres, Set<String> tags) {
        double score = 0.0;
        
        // Calculate genre match score (how many genres match)
        if (!genres.isEmpty()) {
            long genreMatches = game.getGenres().stream()
                    .filter(genres::contains)
                    .count();
            
            double matchRatio = (double) genreMatches / genres.size();
            score += GENRE_WEIGHT * (matchRatio == 1.0 ? 
                    PERFECT_MATCH_SCORE : 
                    PARTIAL_MATCH_BASE_SCORE * matchRatio);
        }
        
        // Calculate tag match score (how many tags match)
        if (!tags.isEmpty()) {
            long tagMatches = game.getTags().stream()
                    .filter(tags::contains)
                    .count();
            
            double matchRatio = (double) tagMatches / tags.size();
            score += TAG_WEIGHT * (matchRatio == 1.0 ? 
                    PERFECT_MATCH_SCORE : 
                    PARTIAL_MATCH_BASE_SCORE * matchRatio);
        }
        
        return (int) Math.round(score);
    }
}