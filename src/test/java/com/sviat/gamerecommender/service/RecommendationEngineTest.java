package com.sviat.gamerecommender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.sviat.gamerecommender.model.Game;
import static com.sviat.gamerecommender.service.RecommendationTestData.*;

/**
 * Tests for RecommendationEngine
 * 
 * REQUIREMENTS:
 * R1. Games with more matching genres should rank higher than games with fewer matching genres
 * R2. Games with more matching tags should rank higher than games with fewer matching tags
 * R3. Genre matches should be weighted higher than tag matches (weight ratio: 1.3:1.0)
 * R4. When match scores are equal, games with higher metacritic scores should rank higher
 * R5. Match quality should consider both the number of matches and the precision of matches
 * R6. Perfect matches (100% of criteria) should score higher than partial matches
 * R7. The system should handle null or empty filter criteria gracefully
 * R8. The system should limit results to the requested number
 */
public class RecommendationEngineTest extends BaseServiceTest {
    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        // Base setup is called first automatically
        recommendationEngine = new RecommendationEngine(gameDatabase);
        TestGameData.getAllTestGames().forEach(gameDatabase::addGame);
    }
    
    @AfterEach
    void tearDown() {
        // Clean up any test-specific games to prevent interference between tests
        cleanTestGames();
    }
    
    private void cleanTestGames() {
        // Get all games currently in the database
        List<Game> allGames = new ArrayList<>(gameDatabase.getAllGames());
        
        // Remove any test-specific games by checking their titles
        allGames.stream()
                .filter(game -> {
                    String title = game.getTitle();
                    return title.contains("Match") || 
                           title.contains("Precision") || 
                           title.contains("Metacritic") ||
                           title.equals("Genre Match") ||
                           title.equals("Tag Match");
                })
                .forEach(gameDatabase::deleteGame);
    }

    @ParameterizedTest
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#recommendationTestCases")
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

    @ParameterizedTest
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#metacriticScoreTestCases")
    void testGetRecommendationsByMetacriticScore(int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByMetacriticScore(limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }

    @ParameterizedTest
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#developerTestCases")
    void testGetRecommendationsByDeveloper(String developer, int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByDeveloper(developer, limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }
    
    @ParameterizedTest
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#releaseDateTestCases")
    void testGetRecommendationsByReleaseDate(int limit, List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getRecommendationsByReleaseDate(limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }
    
    @ParameterizedTest
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#multiFilterTestCases")
    void testGetMultiFilterRecommendations(Set<String> genres, 
                                         Set<String> tags, 
                                         int limit, 
                                         List<String> expectedGameTitles) {
        // Act
        List<String> actualTitles = recommendationEngine.getMultiFilterRecommendations(
                genres,
                tags,
                limit)
                .stream()
                .map(Game::getTitle)
                .toList();        
        // Assert
        assertEquals(expectedGameTitles, actualTitles);
    }  
    
    /**
     * Parameterized test covering requirements R1-R6
     * Each test case verifies that the first game ranks higher than the second
     * based on the specific requirement being tested
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#requirementTestCases")
    void testR1toR6_RecommendationRequirements(
            String testName,
            List<Game> testGames,
            Set<String> genres,
            Set<String> tags,
            String expectedFirstGame,
            String expectedSecondGame) {
        
        testGames.forEach(gameDatabase::addGame);
        
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(genres, tags, 10);
        
        // Filter results to only include our test games
        List<Game> filteredResults = results.stream()
                .filter(g -> g.getTitle().equals(expectedFirstGame) || 
                             g.getTitle().equals(expectedSecondGame))
                .collect(Collectors.toList());
        
        // Verify we found both games
        assertEquals(2, filteredResults.size(), 
                "Both test games should be in the results");
        
        // Verify the ordering is correct
        assertEquals(expectedFirstGame, filteredResults.get(0).getTitle(),
                testName + ": First game should rank higher");
        assertEquals(expectedSecondGame, filteredResults.get(1).getTitle(),
                testName + ": Second game should rank lower");
    }
    
    /**
     * R7. System should handle empty/null filters gracefully
     * When no filters are provided, the system should return games sorted by metacritic score
     */
    @Test
    void testR7_EmptyFiltersHandling() {
        // Test with empty filters - should return games sorted by metacritic score
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                Set.of(), Set.of(), 5);
        
        // Verify we got results despite empty filters
        assertTrue(results.size() > 0, 
                "R7: System should return results even with empty filters");
        
        // Verify results are sorted by metacritic score in descending order
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).getMetacriticScore() >= results.get(i + 1).getMetacriticScore(),
                    String.format("R7: Results should be sorted by metacritic score when filters are empty. "
                            + "Game at position %d has score %d, game at position %d has score %d", 
                            i, results.get(i).getMetacriticScore(), 
                            i+1, results.get(i+1).getMetacriticScore()));
        }
    }
    
    /**
     * R7. System should handle null filters gracefully
     * When null filters are provided, the system should handle them like empty filters
     */
    @Test
    void testR7_NullFiltersHandling() {
        // Test with null filters - should return games sorted by metacritic score
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                null, null, 5);
        
        // Verify we got results despite null filters
        assertTrue(results.size() > 0, 
                "R7: System should return results even with null filters");
        
        // Verify results are sorted by metacritic score in descending order
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).getMetacriticScore() >= results.get(i + 1).getMetacriticScore(),
                    String.format("R7: Results should be sorted by metacritic score when filters are null. "
                            + "Game at position %d has score %d, game at position %d has score %d", 
                            i, results.get(i).getMetacriticScore(), 
                            i+1, results.get(i+1).getMetacriticScore()));
        }
    }
    
    /**
     * R8. The system should limit results to the requested number
     * This test verifies that when a limit is specified, the system returns exactly that many results
     */
    @Test
    void testR8_LimitResults() {
        // Add several games with RPG genre to ensure we have enough for the test
        gameDatabase.addGame(MORE_GENRE_MATCHES);
        gameDatabase.addGame(FEWER_GENRE_MATCHES);
        gameDatabase.addGame(PERFECT_MATCH);
        gameDatabase.addGame(PARTIAL_MATCH);
        
        // Test with limit of 3
        int requestedLimit = 3;
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                Set.of("RPG"), Set.of(), requestedLimit);
        
        // Verify the exact number of results matches the requested limit
        assertEquals(requestedLimit, results.size(), 
                String.format("R8: System should limit results to exactly %d as requested", requestedLimit));
        
        // Also test with a different limit to ensure it's not hardcoded
        int secondLimit = 2;
        List<Game> secondResults = recommendationEngine.getMultiFilterRecommendations(
                Set.of("RPG"), Set.of(), secondLimit);
        
        assertEquals(secondLimit, secondResults.size(),
                String.format("R8: System should limit results to exactly %d as requested", secondLimit));
    }
    
    /**
     * Edge case: System should handle non-existent genres and tags gracefully
     * When filters contain genres or tags that don't exist in any game,
     * the system should still return results based on partial matches
     */
    @Test
    void testEdgeCase_NonExistentFilters() {
        // Test with non-existent genre and tag
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                Set.of("NonExistentGenre", "RPG"), 
                Set.of("NonExistentTag", "Open World"), 
                10);
        
        // Should still return results that match the valid parts of the filter
        assertTrue(results.size() > 0, 
                "System should return results even with some non-existent filters");
        
        // Verify that games with RPG genre or Open World tag are included
        boolean hasMatchingGame = results.stream()
                .anyMatch(game -> 
                    game.getGenres().contains("RPG") || 
                    game.getTags().contains("Open World"));
        
        assertTrue(hasMatchingGame, 
                "Results should include games matching the valid parts of the filter");
    }
    
    /**
     * Edge case: System should handle completely non-matching filters gracefully
     * When no games match any of the filter criteria, the system should return an empty list
     * rather than throwing an exception
     */
    @Test
    void testEdgeCase_ZeroResults() {
        // Use filters that won't match any games in the database
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                Set.of("CompletelyNonExistentGenre"), 
                Set.of("CompletelyNonExistentTag"), 
                10);
        
        // Should return an empty list, not throw an exception
        assertNotNull(results, "Result should be an empty list, not null");
        assertTrue(results.isEmpty(), "No games should match these completely non-existent filters");
    }
    
    /**
     * Tests the direct score calculation logic using reflection to access private method
     * This verifies the core scoring algorithm works correctly for different scenarios
     */
    @ParameterizedTest(name = "Game: {0}, Genres: {1}, Tags: {2}, Expected Score: {3}")
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#scoreCalculationTestCases")
    void testScoreCalculationAlgorithm(String gameTitle, Set<String> genres, Set<String> tags, int expectedScore) throws Exception {
        // Get the private calculateMatchScore method using reflection
        Method calculateMatchScore = RecommendationEngine.class.getDeclaredMethod(
                "calculateMatchScore", Game.class, Set.class, Set.class);
        calculateMatchScore.setAccessible(true);
        
        // Find the game by title
        Game game = gameDatabase.getAllGames().stream()
                .filter(g -> g.getTitle().equals(gameTitle))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test game not found: " + gameTitle));
        
        // Calculate the score using reflection
        int actualScore = (int) calculateMatchScore.invoke(recommendationEngine, game, genres, tags);
        
        // Assert with descriptive message
        assertEquals(expectedScore, actualScore, 
                String.format("Score calculation failed for game '%s' with genres %s and tags %s", 
                        gameTitle, genres, tags));
    }
    
    /**
     * Tests how genre and tag weights influence the ordering of recommendations
     * This verifies that the weighting system correctly prioritizes games
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#weightInfluenceTestCases")
    void testWeightPriorityInfluence(String testName, Set<String> genres, Set<String> tags, int limit, List<String> expectedOrder) {
        // Get recommendations based on test parameters
        List<Game> results = recommendationEngine.getMultiFilterRecommendations(genres, tags, limit);
        
        // Extract titles for easier comparison
        List<String> actualTitles = results.stream()
                .map(Game::getTitle)
                .collect(Collectors.toList());
        
        // Filter to only include the expected games
        List<String> filteredTitles = actualTitles.stream()
                .filter(expectedOrder::contains)
                .collect(Collectors.toList());
        
        // Verify the ordering matches expected
        assertEquals(expectedOrder, filteredTitles, 
                String.format("%s: Games should be ordered correctly based on weight influence", testName));
        
        // Verify we found all expected games
        assertEquals(expectedOrder.size(), filteredTitles.size(),
                String.format("%s: All expected games should be in the results", testName));
    }
}