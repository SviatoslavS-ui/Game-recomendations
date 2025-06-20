package com.sviat.gamerecommender.service;

import com.sviat.gamerecommender.model.Game;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

public class TestGameData {
    public static final Game RPG_ACTION = Game.builder()
            .id("game1")
            .title("RPG Action Game")
            .genres(Set.of("RPG", "Action"))
            .build();

    public static final Game RPG_ACTION_STRATEGY = Game.builder()
            .id("game2")
            .title("RPG Action Strategy")
            .genres(Set.of("RPG", "Action", "Strategy"))
            .build();

    public static final Game ACTION_ONLY = Game.builder()
            .id("game3")
            .title("Pure Action")
            .genres(Set.of("Action"))
            .build();

    public static final Game STRATEGY_ONLY = Game.builder()
            .id("game4")
            .title("Pure Strategy")
            .genres(Set.of("Strategy"))
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

    public static final Game DATABASE_TEST = Game.builder()
            .id("test-game")
            .title("Test Game")
            .build();

    public static final Game DATABASE_TEST_ORIGINAL = Game.builder()
            .id("test-game")
            .title("Original Title")
            .build();

    public static final Game DATABASE_TEST_UPDATED = Game.builder()
            .id("test-game")
            .title("Updated Title")
            .build();

    public static List<Game> getAllTestGames() {
        return Arrays.asList(RPG_ACTION, RPG_ACTION_STRATEGY, ACTION_ONLY, STRATEGY_ONLY);
    }

    public static List<Game> getSerializationTestGames() {
        return Arrays.asList(SERIALIZATION_TEST_1, SERIALIZATION_TEST_2);
    }
}