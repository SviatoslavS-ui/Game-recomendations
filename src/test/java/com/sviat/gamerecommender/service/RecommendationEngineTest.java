package com.sviat.gamerecommender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sviat.gamerecommender.model.Game;

public class RecommendationEngineTest extends BaseServiceTest {
    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        // Base setup is called first automatically
        recommendationEngine = new RecommendationEngine(gameDatabase);
        TestGameData.getAllTestGames().forEach(gameDatabase::addGame);
    }

    // Test data for genre recommendations
    private static Stream<Arguments> recommendationTestCases() {
        return Stream.of(
            // Format: searchGenres, limit, expectedGameTitles
            Arguments.of(
                Set.of("RPG", "Action"),
                4,
                List.of(
                    "RPG Action Game",
                    "RPG Action Strategy",
                    "Pure Action"
                )
            ),
            Arguments.of(
                Set.of("RPG", "Action", "Strategy"),
                4,
                List.of(
                    "RPG Action Strategy",
                    "RPG Action Game",
                    "Pure Action",
                    "Pure Strategy"
                )
            ),
            Arguments.of(
                Set.of("RPG", "Action", "Strategy"),
                2,
                List.of(
                    "RPG Action Strategy",
                    "RPG Action Game"
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("recommendationTestCases")
    void testRecommendations(Set<String> searchGenres, 
                           int limit, 
                           List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByGenre(searchGenres, limit)
                .stream()
                .map(Game::getTitle)
                .toList();
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }

    // Test data for Metacritic score recommendations
    private static Stream<Arguments> metacriticScoreTestCases() {
        return Stream.of(
            // Format: limit, expectedGameTitles (ordered by score descending)
            Arguments.of(
                3,
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Console Exclusive"      // 89
                )
            ),
            Arguments.of(
                5,
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Console Exclusive",     // 89
                    "Pure Strategy",         // 88
                    "RPG Action Game"        // 85
                )
            ),
            Arguments.of(
                1,
                List.of("Masterpiece Game") // Only the highest scored
            ),
            Arguments.of(
                10, // More than available games
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Console Exclusive",     // 89
                    "Pure Strategy",         // 88
                    "RPG Action Game",       // 85
                    "Cross Platform Hit",    // 82
                    "Pure Action",           // 78
                    "Budget Game"            // 65
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("metacriticScoreTestCases")
    void testGetRecommendationsByMetacriticScore(int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByMetacriticScore(limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }

    // Test data for developer recommendations
    private static Stream<Arguments> developerTestCases() {
        return Stream.of(
            // Format: developer, limit, expectedGameTitles (ordered by score descending)
            Arguments.of(
                "GameStudio A",
                5,
                List.of(
                    "RPG Action Game",       // 85
                    "Cross Platform Hit",    // 82
                    "Pure Action"            // 78
                )
            ),
            Arguments.of(
                "GameStudio B",
                5,
                List.of("RPG Action Strategy") // Only one game
            ),
            Arguments.of(
                "Elite Studios",
                5,
                List.of("Masterpiece Game")
            ),
            Arguments.of(
                "GameStudio A",
                2, // Limit smaller than available games
                List.of(
                    "RPG Action Game",       // 85
                    "Cross Platform Hit"     // 82
                )
            ),
            Arguments.of(
                "NonExistent Developer",
                5,
                List.of() // Empty result
            )
        );
    }

    @ParameterizedTest
    @MethodSource("developerTestCases")
    void testGetRecommendationsByDeveloper(String developer, int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByDeveloper(developer, limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }

    // Test data for platform recommendations
    private static Stream<Arguments> platformTestCases() {
        return Stream.of(
            // Format: platform, limit, expectedGameTitles (ordered by score descending)
            Arguments.of(
                "PC",
                5,
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Pure Strategy",         // 88
                    "RPG Action Game",       // 85
                    "Cross Platform Hit"     // 82
                    // Budget Game (65) would be next
                )
            ),
            Arguments.of(
                "PlayStation",
                4,
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Console Exclusive",     // 89
                    "RPG Action Game"        // 85
                )
            ),
            Arguments.of(
                "Xbox",
                3,
                List.of(
                    "Masterpiece Game",      // 96
                    "RPG Action Strategy",   // 92
                    "Cross Platform Hit"     // 82
                )
            ),
            Arguments.of(
                "Nintendo Switch",
                2,
                List.of(
                    "Masterpiece Game",      // 96
                    "Cross Platform Hit"     // 82
                )
            ),
            Arguments.of(
                "Mobile",
                5,
                List.of("Cross Platform Hit") // Only one game
            ),
            Arguments.of(
                "NonExistent Platform",
                5,
                List.of() // Empty result
            )
        );
    }

    @ParameterizedTest
    @MethodSource("platformTestCases")
    void testGetRecommendationsByPlatform(String platform, int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByPlatform(platform, limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }
}