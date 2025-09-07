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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.sviat.gamerecommender.model.Game;
import static com.sviat.gamerecommender.service.RecommendationTestData.*;

/**
 * Tests for RecommendationEngine
 * 
 * REQUIREMENTS:
 * R1. Games with more matching genres should rank higher than games with fewer
 * matching genres
 * R2. Games with more matching tags should rank higher than games with fewer
 * matching tags
 * R3. Genre matches should be weighted higher than tag matches (weight ratio:
 * 1.3:1.0)
 * R4. When match scores are equal, games with higher metacritic scores should
 * rank higher
 * R5. Match quality should consider both the number of matches and the
 * precision of matches
 * R6. Perfect matches (100% of criteria) should score higher than partial
 * matches
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

        @Test
        @Tag("Manual")
        void testRecomendationForParticularDataSet() {
                // Arrange
                gameDatabase.loadGamesFromFile("src/main/resources/data/games.json");

                // Test Case 1: Valid genre search - should return games
                Set<String> validGenre = Set.of("Real-Time Tactics");

                // Act - Get recommendations with valid genre
                List<String> actualTitlesWithValidGenre = recommendationEngine
                                .getMultiFilterRecommendations(validGenre, null, 10)
                                .stream()
                                .map(Game::getTitle)
                                .toList();

                // Assert - Should return games matching the Real-Time Tactics genre
                System.out.println("Actual titles with valid genre: " + actualTitlesWithValidGenre);
                assertTrue(actualTitlesWithValidGenre.size() > 0,
                                "Should return at least one game with Real-Time Tactics genre");

                // Verify all returned games have the Real-Time Tactics genre
                for (String title : actualTitlesWithValidGenre) {
                        Game game = gameDatabase.getAllGames().stream()
                                        .filter(g -> g.getTitle().equals(title))
                                        .findFirst()
                                        .orElse(null);
                        assertNotNull(game, "Game should exist in database");
                        assertTrue(game.getGenres().contains("Real-Time Tactics"),
                                        "Game should have Real-Time Tactics genre: " + title);
                }

                // Test Case 2: No criteria provided - should return empty list
                List<Game> noFilterResults = recommendationEngine
                                .getMultiFilterRecommendations(Set.of(), Set.of(), 10);

                // Assert - Should return empty list when no criteria provided
                assertTrue(noFilterResults.isEmpty(),
                                "Should return empty list when no search criteria provided");

                // Test Case 3: Non-existent genre - should return empty list
                List<Game> nonExistentGenreResults = recommendationEngine
                                .getMultiFilterRecommendations(Set.of("NonExistentGenre"), null, 10);

                // Assert - Should return empty list when no matches found
                assertTrue(nonExistentGenreResults.isEmpty(),
                                "Should return empty list when no matches found for genre");

                // Test Case 4: Empty string in criteria - should be filtered out and treated as
                // no criteria
                List<Game> emptyStringResults = recommendationEngine
                                .getMultiFilterRecommendations(Set.of(""), Set.of(""), 10);

                // Assert - Should return empty list when only empty strings provided
                assertTrue(emptyStringResults.isEmpty(),
                                "Should return empty list when only empty strings provided as criteria");
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

                // Create a copy of the games to avoid ConcurrentModificationException
                List<Game> gamesToDelete = new ArrayList<>(gameDatabase.getAllGames());
                gamesToDelete.forEach(game -> gameDatabase.deleteGame(game));
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
         * When no filters are provided, the system should return an empty list
         */
        @Test
        void testR7_EmptyFiltersHandling() {
                // Test with empty filters - should return empty list
                List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                                Set.of(), Set.of(), 5);

                // Verify we got an empty list with empty filters
                assertTrue(results.isEmpty(),
                                "R7: System should return empty list when no filters are provided");
        }

        /**
         * R7. System should handle null filters gracefully
         * When null filters are provided, the system should return an empty list
         */
        @Test
        void testR7_NullFiltersHandling() {
                // Test with null filters - should return empty list
                List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                                null, null, 5);

                // Verify we got an empty list with null filters
                assertTrue(results.isEmpty(),
                                "R7: System should return empty list when null filters are provided");
        }

        /**
         * R8. The system should limit results to the requested number
         * This test verifies that when a limit is specified, the system returns exactly
         * that many results
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
                                String.format("R8: System should limit results to exactly %d as requested",
                                                requestedLimit));

                // Also test with a different limit to ensure it's not hardcoded
                int secondLimit = 2;
                List<Game> secondResults = recommendationEngine.getMultiFilterRecommendations(
                                Set.of("RPG"), Set.of(), secondLimit);

                assertEquals(secondLimit, secondResults.size(),
                                String.format("R8: System should limit results to exactly %d as requested",
                                                secondLimit));
        }

        /**
         * Edge case: System should handle mixed valid/non-existent filters gracefully
         * When filters contain both valid and non-existent genres/tags,
         * the system should return only games that match the valid parts
         */
        @Test
        void testEdgeCase_NonExistentFilters() {
                // Add test games with RPG genre
                gameDatabase.addGame(GENRE_MATCH); // Has RPG genre

                // Test with mix of non-existent and valid genre
                List<Game> results = recommendationEngine.getMultiFilterRecommendations(
                                Set.of("NonExistentGenre", "RPG"),
                                null,
                                10);

                // Should return only games with RPG genre
                assertTrue(results.size() > 0,
                                "System should return results that match the valid parts of the filter");

                // Verify that all returned games have RPG genre
                boolean allHaveRpgGenre = results.stream()
                                .allMatch(game -> game.getGenres().contains("RPG"));

                assertTrue(allHaveRpgGenre,
                                "All results should have RPG genre when filtering with mixed valid/invalid genres");
        }

        /**
         * Edge case: System should handle completely non-matching filters gracefully
         * When no games match any of the filter criteria, the system should return an
         * empty list
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
         * Tests the direct score calculation logic using reflection to access private
         * method
         * This verifies the core scoring algorithm works correctly for different
         * scenarios
         */
        @ParameterizedTest(name = "Game: {0}, Genres: {1}, Tags: {2}, Expected Score: {3}")
        @MethodSource("com.sviat.gamerecommender.service.RecommendationTestData#scoreCalculationTestCases")
        void testScoreCalculationAlgorithm(String gameTitle, Set<String> genres, Set<String> tags, int expectedScore)
                        throws Exception {
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
        void testWeightPriorityInfluence(String testName, Set<String> genres, Set<String> tags, int limit,
                        List<String> expectedOrder) {
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
                                String.format("%s: Games should be ordered correctly based on weight influence",
                                                testName));

                // Verify we found all expected games
                assertEquals(expectedOrder.size(), filteredTitles.size(),
                                String.format("%s: All expected games should be in the results", testName));
        }
}