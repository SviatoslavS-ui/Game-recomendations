package com.sviat.gamerecommender.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
}