package com.sviat.gamerecommender.service;

import com.sviat.gamerecommender.model.Game;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.Arrays;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Test data for recommendation engine tests.
 * Contains game data and test case data for parameterized tests.
 * 
 * This class centralizes all test data to improve maintainability and reusability.
 * Each test game and test case is documented with the requirement it helps test.
 */
public class RecommendationTestData {

    // Test games for requirement-based tests
    public static final Game MORE_GENRE_MATCHES = Game.builder()
            .id("more-matches")
            .title("More Genre Matches")
            .genres(Set.of("RPG", "Action"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game FEWER_GENRE_MATCHES = Game.builder()
            .id("fewer-matches")
            .title("Fewer Genre Matches")
            .genres(Set.of("RPG"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game MORE_TAG_MATCHES = Game.builder()
            .id("more-tag-matches")
            .title("More Tag Matches")
            .genres(Set.of())
            .tags(Set.of("Open World", "Story Rich"))
            .metacriticScore(80)
            .build();
            
    public static final Game FEWER_TAG_MATCHES = Game.builder()
            .id("fewer-tag-matches")
            .title("Fewer Tag Matches")
            .genres(Set.of())
            .tags(Set.of("Open World"))
            .metacriticScore(80)
            .build();
            
    public static final Game GENRE_MATCH = Game.builder()
            .id("genre-match")
            .title("Genre Match")
            .genres(Set.of("RPG"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game TAG_MATCH = Game.builder()
            .id("tag-match")
            .title("Tag Match")
            .genres(Set.of())
            .tags(Set.of("Open World"))
            .metacriticScore(80)
            .build();
            
    public static final Game HIGH_METACRITIC = Game.builder()
            .id("high-metacritic")
            .title("High Metacritic")
            .genres(Set.of("RPG"))
            .tags(Set.of("Open World"))
            .metacriticScore(90)
            .build();
            
    public static final Game LOW_METACRITIC = Game.builder()
            .id("low-metacritic")
            .title("Low Metacritic")
            .genres(Set.of("RPG"))
            .tags(Set.of("Open World"))
            .metacriticScore(70)
            .build();
            
    public static final Game HIGH_PRECISION = Game.builder()
            .id("high-precision")
            .title("High Precision")
            .genres(Set.of("RPG", "Action"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game LOW_PRECISION = Game.builder()
            .id("low-precision")
            .title("Low Precision")
            .genres(Set.of("RPG", "Action", "Strategy", "Simulation", "Puzzle"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game PERFECT_MATCH = Game.builder()
            .id("perfect-match")
            .title("Perfect Match")
            .genres(Set.of("RPG", "Action"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();
            
    public static final Game PARTIAL_MATCH = Game.builder()
            .id("partial-match")
            .title("Partial Match")
            .genres(Set.of("RPG", "Strategy"))
            .tags(Set.of())
            .metacriticScore(80)
            .build();

    /**
     * Test data for direct score calculation tests.
     * Tests the core scoring algorithm with different combinations of genre and tag matches.
     * 
     * @return Stream of test cases for score calculation tests
     */
    public static Stream<Arguments> scoreCalculationTestCases() {
        return Stream.of(
            // Format: game title, genres, tags, expected score
            Arguments.of(
                "RPG Action Game",
                Set.of("RPG", "Action"),  // exact match for game's genres
                Set.of(),                 // no tags
                200                       // 100 * 2.0 genre weight for perfect match
            ),
            Arguments.of(
                "RPG Action Game",
                Set.of("RPG"),         // partial match (1/1 of search genres)
                Set.of(),                 // no tags
                200                       // 100 * 2.0 genre weight for perfect match ratio
            ),
            Arguments.of(
                "RPG Action Game",
                Set.of(),                 // no genres
                Set.of("Open World"),  // exact match for one of game's tags
                100                       // 100 * 1.0 tag weight for perfect match ratio
            ),
            Arguments.of(
                "Masterpiece Game",
                Set.of("Adventure"),   // exact match for game's genre
                Set.of("Atmospheric"), // exact match for game's tag
                300                       // (100 * 2.0) + (100 * 1.0) = 300
            ),
            Arguments.of(
                "RPG Action Strategy",
                Set.of("RPG", "Strategy"), // perfect match ratio (2/2 of search genres)
                Set.of("Story Rich"),  // exact match for game's tag
                300                       // (100 * 2.0) + (100 * 1.0) = 300
            ),
            // New test cases for partial matches with new constants
            Arguments.of(
                "RPG Action Game",
                Set.of("RPG", "Action", "Strategy"),  // 2/3 match for search genres
                Set.of(),                 // no tags
                80                        // 60 * 2.0 * (2/3) = 80
            ),
            Arguments.of(
                "RPG Action Game",
                Set.of("RPG", "Action", "Strategy"),  // 2/3 match for search genres
                Set.of("Open World", "Story Rich", "Multiplayer"),  // 2/3 match for search tags
                120                       // (60 * 2.0 * 2/3) + (60 * 1.0 * 2/3) = 80 + 40 = 120
            )
        );
    }

    /**
     * Test data for weight influence tests.
     * Tests how genre and tag weights affect recommendation ordering.
     * 
     * @return Stream of test cases for weight influence tests
     */
    public static Stream<Arguments> weightInfluenceTestCases() {
        return Stream.of(
            // Format: test name, genres, tags, limit, expected game order
            Arguments.of(
                "Genre and tag match with metacritic score ordering",
                Set.of("RPG"),                  // Both games have RPG genre
                Set.of("Open World"),           // Only RPG Action Game has Open World tag
                2,
                List.of("RPG Action Game")
                // Only RPG Action Game matches both genre and tag
            ),
            Arguments.of(
                "Perfect genre and tag match",
                Set.of("Adventure"),            // Perfect match for Masterpiece Game's genre
                Set.of("Atmospheric"),          // Perfect match for Masterpiece Game's tag
                2,
                List.of("Masterpiece Game")
                // Only Masterpiece Game matches both criteria
            ),
            Arguments.of(
                "Multiple genres with tag match",
                Set.of("RPG", "Strategy"),      // Both games have RPG genre
                Set.of("Story Rich"),           // Only RPG Action Strategy has Story Rich tag
                2,
                List.of("RPG Action Strategy")
                // Only RPG Action Strategy matches both criteria
            )
        );
    }
    
    /**
     * Test data for requirement-based tests (R1-R6).
     * Each test case verifies a specific requirement with appropriate test games.
     * 
     * @return Stream of test cases for requirements R1-R6
     */
    public static Stream<Arguments> requirementTestCases() {
        return Stream.of(
            // R1: Games with more matching genres should rank higher
            Arguments.of(
                "R1: More matching genres rank higher",
                List.of(MORE_GENRE_MATCHES, FEWER_GENRE_MATCHES),
                Set.of("RPG", "Action"), 
                Set.of(),
                "More Genre Matches",
                "Fewer Genre Matches"
            ),
            // R2: Games with more matching tags should rank higher
            Arguments.of(
                "R2: More matching tags rank higher",
                List.of(MORE_TAG_MATCHES, FEWER_TAG_MATCHES),
                Set.of(), 
                Set.of("Open World", "Story Rich"),
                "More Tag Matches",
                "Fewer Tag Matches"
            ),
            // R3: Genre matches weighted higher than tag matches
            Arguments.of(
                "R3: Genre matches weighted higher than tag matches",
                List.of(GENRE_MATCH, TAG_MATCH),
                Set.of("RPG"), 
                Set.of("Open World"),
                "Genre Match",
                "Tag Match"
            ),
            // R4: Metacritic score as tiebreaker
            Arguments.of(
                "R4: Metacritic score as tiebreaker",
                List.of(HIGH_METACRITIC, LOW_METACRITIC),
                Set.of("RPG"), 
                Set.of("Open World"),
                "High Metacritic",
                "Low Metacritic"
            ),
            // R5: Match quality considers precision
            Arguments.of(
                "R5: Match quality considers precision",
                List.of(HIGH_PRECISION, LOW_PRECISION),
                Set.of("RPG", "Action"), 
                Set.of(),
                "High Precision",
                "Low Precision"
            ),
            // R6: Perfect matches score higher than partial matches
            Arguments.of(
                "R6: Perfect matches score higher than partial matches",
                List.of(PERFECT_MATCH, PARTIAL_MATCH),
                Set.of("RPG", "Action"), 
                Set.of(),
                "Perfect Match",
                "Partial Match"
            )
        );
    }
    
    
    /**
     * Test data for basic genre recommendation tests.
     * Tests various combinations of genre filters with different limits.
     * 
     * @return Stream of test cases for genre-based recommendations
     */
    public static Stream<Arguments> recommendationTestCases() {
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
    
    /**
     * Test data for Metacritic score recommendations.
     * Tests ordering of games by metacritic score with different limits.
     * 
     * @return Stream of test cases for metacritic score recommendations
     */
    public static Stream<Arguments> metacriticScoreTestCases() {
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
    
    /**
     * Test data for developer recommendations.
     * Tests filtering games by developer with different limits.
     * 
     * @return Stream of test cases for developer recommendations
     */
    public static Stream<Arguments> developerTestCases() {
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
    
    /**
     * Test data for release date recommendations.
     * Tests ordering of games by release date with different limits.
     * 
     * @return Stream of test cases for release date recommendations
     */
    public static Stream<Arguments> releaseDateTestCases() {
        return Stream.of(
            // Format: limit, expectedGameTitles (ordered by release date descending - newest first)
            Arguments.of(
                3,
                List.of(
                    "Masterpiece Game",     // 2022-02-25
                    "Console Exclusive",    // 2021-11-19
                    "Pure Strategy"         // 2021-06-30
                )
            ),
            Arguments.of(
                5,
                List.of(
                    "Masterpiece Game",     // 2022-02-25
                    "Console Exclusive",    // 2021-11-19
                    "Pure Strategy",        // 2021-06-30
                    "RPG Action Strategy",  // 2020-11-10
                    "Cross Platform Hit"    // 2020-09-04
                )
            ),
            Arguments.of(
                8, // All games
                List.of(
                    "Masterpiece Game",     // 2022-02-25
                    "Console Exclusive",    // 2021-11-19
                    "Pure Strategy",        // 2021-06-30
                    "RPG Action Strategy",  // 2020-11-10
                    "Cross Platform Hit",   // 2020-09-04
                    "RPG Action Game",      // 2019-05-15
                    "Pure Action",          // 2018-03-20
                    "Budget Game"           // 2017-08-12
                )
            ),
            Arguments.of(
                2, // Just the newest
                List.of(
                    "Masterpiece Game",     // 2022-02-25
                    "Console Exclusive"     // 2021-11-19
                )
            )
        );
    }
    
    /**
     * Get all test games for requirement-based tests
     * 
     * @return List of all test games used in requirement tests
     */
    public static List<Game> getAllRequirementTestGames() {
        return Arrays.asList(
            MORE_GENRE_MATCHES, FEWER_GENRE_MATCHES,
            MORE_TAG_MATCHES, FEWER_TAG_MATCHES,
            GENRE_MATCH, TAG_MATCH,
            HIGH_METACRITIC, LOW_METACRITIC,
            HIGH_PRECISION, LOW_PRECISION,
            PERFECT_MATCH, PARTIAL_MATCH
        );
    }
}
