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
            .tags(Set.of("Open World", "Story Rich", "Singleplayer"))
            .metacriticScore(85)
            .developer("GameStudio A")
            .platforms(Set.of("PC", "PlayStation"))
            .releaseDate("2019-05-15")
            .build();

    public static final Game RPG_ACTION_STRATEGY = Game.builder()
            .id("game2")
            .title("RPG Action Strategy")
            .genres(Set.of("RPG", "Action", "Strategy"))
            .tags(Set.of("Story Rich", "Difficult", "Tactical"))
            .metacriticScore(92)
            .developer("GameStudio B")
            .platforms(Set.of("PC", "Xbox", "PlayStation"))
            .releaseDate("2020-11-10")
            .build();

    public static final Game ACTION_ONLY = Game.builder()
            .id("game3")
            .title("Pure Action")
            .genres(Set.of("Action"))
            .tags(Set.of("Fast-Paced", "Combat", "Arcade"))
            .metacriticScore(78)
            .developer("GameStudio A")
            .platforms(Set.of("Xbox"))
            .releaseDate("2018-03-20")
            .build();

    public static final Game STRATEGY_ONLY = Game.builder()
            .id("game4")
            .title("Pure Strategy")
            .genres(Set.of("Strategy"))
            .tags(Set.of("Tactical", "Turn-Based", "Resource Management"))
            .metacriticScore(88)
            .developer("GameStudio C")
            .platforms(Set.of("PC"))
            .releaseDate("2021-06-30")
            .build();

    public static final Game HIGH_SCORE_GAME = Game.builder()
            .id("game5")
            .title("Masterpiece Game")
            .genres(Set.of("Adventure"))
            .tags(Set.of("Atmospheric", "Story Rich", "Open World", "Exploration"))
            .metacriticScore(96)
            .developer("Elite Studios")
            .platforms(Set.of("PC", "PlayStation", "Xbox", "Nintendo Switch"))
            .releaseDate("2022-02-25")
            .build();

    public static final Game DATABASE_TEST = Game.builder()
            .id("test-game")
            .title("Test Game")
            .genres(Set.of("Test", "Game"))
            .tags(Set.of("Test", "Sample"))
            .metacriticScore(90)
            .developer("Test Studio")
            .platforms(Set.of("PC"))
            .build();

    public static final Game DATABASE_TEST_ORIGINAL = Game.builder()
            .id("test-game-original")
            .title("Original Test Game")
            .genres(Set.of("Test", "Original"))
            .tags(Set.of("Test", "Original"))
            .metacriticScore(85)
            .developer("Test Studio Original")
            .platforms(Set.of("PC"))
            .build();

    public static final Game LOW_SCORE_GAME = Game.builder()
            .id("game6")
            .title("Budget Game")
            .genres(Set.of("Casual"))
            .tags(Set.of("Indie", "Short", "Casual"))
            .metacriticScore(65)
            .developer("Indie Dev")
            .platforms(Set.of("PC"))
            .releaseDate("2017-08-12")
            .build();

    public static final Game MULTI_PLATFORM_GAME = Game.builder()
            .id("game7")
            .title("Cross Platform Hit")
            .genres(Set.of("Sports"))
            .tags(Set.of("Multiplayer", "Competitive", "Team-Based"))
            .metacriticScore(82)
            .developer("GameStudio A")
            .platforms(Set.of("PC", "PlayStation", "Xbox", "Nintendo Switch", "Mobile"))
            .releaseDate("2020-09-04")
            .build();

    public static final Game EXCLUSIVE_GAME = Game.builder()
            .id("game8")
            .title("Console Exclusive")
            .genres(Set.of("Racing"))
            .tags(Set.of("Realistic", "Simulation", "Competitive"))
            .metacriticScore(89)
            .developer("Console Studios")
            .platforms(Set.of("PlayStation"))
            .releaseDate("2021-11-19")
            .build();

    public static final Game SERIALIZATION_TEST_1 = Game.builder()
            .title("Test Game 1")
            .genres(Set.of("Action"))
            .tags(Set.of("Test"))
            .metacriticScore(85)
            .build();

    public static final Game SERIALIZATION_TEST_2 = Game.builder()
            .title("Test Game 2")
            .genres(Set.of("RPG"))
            .tags(Set.of("Test"))
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