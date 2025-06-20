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
}