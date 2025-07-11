package com.sviat.gamerecommender.service;

import com.sviat.gamerecommender.model.Game;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

public class TestGameData {
    /**
     * Simple HTML content for basic game details testing.
     * This is used in unit tests to verify the S3 upload and retrieval functionality.
     */
    public static final String SIMPLE_GAME_DETAILS_HTML = "<div class=\"game-details\">\n" +
            "  <h2>Test Game Details</h2>\n" +
            "  <p>This is a test description for game details storage.</p>\n" +
            "  <ul>\n" +
            "    <li>Feature 1: Amazing gameplay</li>\n" +
            "    <li>Feature 2: Stunning graphics</li>\n" +
            "    <li>Feature 3: Immersive story</li>\n" +
            "  </ul>\n" +
            "</div>";    

    public static final Game RPG_ACTION = Game.builder()
            .id("game1")
            .title("RPG Action Game")
            .genres(Set.of("RPG", "Action"))
            .metacriticScore(85)
            .developer("GameStudio A")
            .platforms(Set.of("PC", "PlayStation"))
            .build();

    public static final Game RPG_ACTION_STRATEGY = Game.builder()
            .id("game2")
            .title("RPG Action Strategy")
            .genres(Set.of("RPG", "Action", "Strategy"))
            .metacriticScore(92)
            .developer("GameStudio B")
            .platforms(Set.of("PC", "Xbox", "PlayStation"))
            .build();

    public static final Game ACTION_ONLY = Game.builder()
            .id("game3")
            .title("Pure Action")
            .genres(Set.of("Action"))
            .metacriticScore(78)
            .developer("GameStudio A")
            .platforms(Set.of("Xbox"))
            .build();

    public static final Game STRATEGY_ONLY = Game.builder()
            .id("game4")
            .title("Pure Strategy")
            .genres(Set.of("Strategy"))
            .metacriticScore(88)
            .developer("GameStudio C")
            .platforms(Set.of("PC"))
            .build();

    public static final Game HIGH_SCORE_GAME = Game.builder()
            .id("game5")
            .title("Masterpiece Game")
            .genres(Set.of("Adventure"))
            .metacriticScore(96)
            .developer("Elite Studios")
            .platforms(Set.of("PC", "PlayStation", "Xbox", "Nintendo Switch"))
            .build();

    public static final Game DATABASE_TEST = Game.builder()
            .id("test-game")
            .title("Test Game")
            .genres(Set.of("Test", "Game"))
            .metacriticScore(90)
            .developer("Test Studio")
            .platforms(Set.of("PC"))
            .build();

    public static final Game DATABASE_TEST_ORIGINAL = Game.builder()
            .id("test-game-original")
            .title("Original Test Game")
            .genres(Set.of("Test", "Original"))
            .metacriticScore(85)
            .developer("Test Studio Original")
            .platforms(Set.of("PC"))
            .build();

    public static final Game LOW_SCORE_GAME = Game.builder()
            .id("game6")
            .title("Budget Game")
            .genres(Set.of("Casual"))
            .metacriticScore(65)
            .developer("Indie Dev")
            .platforms(Set.of("PC"))
            .build();

    public static final Game MULTI_PLATFORM_GAME = Game.builder()
            .id("game7")
            .title("Cross Platform Hit")
            .genres(Set.of("Sports"))
            .metacriticScore(82)
            .developer("GameStudio A")
            .platforms(Set.of("PC", "PlayStation", "Xbox", "Nintendo Switch", "Mobile"))
            .build();

    public static final Game EXCLUSIVE_GAME = Game.builder()
            .id("game8")
            .title("Console Exclusive")
            .genres(Set.of("Racing"))
            .metacriticScore(89)
            .developer("Console Studios")
            .platforms(Set.of("PlayStation"))
            .build();

    public static final Game SERIALIZATION_TEST_1 = Game.builder()
            .title("Test Game 1")
            .genres(Set.of("Action"))
            .metacriticScore(85)
            .build();

    public static final Game SERIALIZATION_TEST_2 = Game.builder()
            .title("Test Game 2")
            .genres(Set.of("RPG"))
            .metacriticScore(90)
            .build();


    public static List<Game> getAllTestGames() {
        return Arrays.asList(
            RPG_ACTION, RPG_ACTION_STRATEGY, ACTION_ONLY, STRATEGY_ONLY,
            HIGH_SCORE_GAME, LOW_SCORE_GAME, MULTI_PLATFORM_GAME, EXCLUSIVE_GAME
        );
    }

    public static List<Game> getSerializationTestGames() {
        return Arrays.asList(SERIALIZATION_TEST_1, SERIALIZATION_TEST_2);
    }
}