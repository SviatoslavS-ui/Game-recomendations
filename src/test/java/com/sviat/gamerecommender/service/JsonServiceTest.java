package com.sviat.gamerecommender.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sviat.gamerecommender.model.Game;

public class JsonServiceTest extends BaseServiceTest {
    private static final String GAMES_JSON_PATH = "src/main/resources/data/games.json";

    @Test
    void loadFromFile_ShouldDeserializeGamesFromJson() {
        // Arrange & Act
        List<Game> games = jsonService.loadFromFile(GAMES_JSON_PATH, new TypeReference<List<Game>>() {});

        // Assert
        assertAll("Game list validations",
            () -> assertNotNull(games, "Games list should not be null"),
            () -> assertFalse(games.isEmpty(), "Games list should not be empty"),
            () -> games.forEach(game -> 
                assertAll("Game properties",
                    () -> assertNotNull(game.getTitle(), "Game title should not be null"),
                    () -> assertNotNull(game.getGenres(), "Game genres should not be null")
                )
            )
        );
    }

    @Test
    void saveAndLoadFile_ShouldPreserveData(@TempDir Path tempDir) {
        // Arrange
        List<Game> originalGames = TestGameData.getSerializationTestGames();
        String testFilePath = tempDir.resolve("test-games.json").toString();

        // Act
        jsonService.saveToFile(testFilePath, originalGames);
        List<Game> loadedGames = jsonService.loadFromFile(testFilePath, new TypeReference<List<Game>>() {});

        // Assert
        assertAll("Game list comparison",
            () -> assertEquals(originalGames.size(), loadedGames.size(), "Lists should have same size"),
            () -> IntStream.range(0, originalGames.size()).forEach(i -> 
                assertAll("Game " + (i + 1) + " properties",
                    () -> assertEquals(originalGames.get(i).getTitle(), loadedGames.get(i).getTitle()),
                    () -> assertEquals(originalGames.get(i).getGenres(), loadedGames.get(i).getGenres()),
                    () -> assertEquals(originalGames.get(i).getMetacriticScore(), loadedGames.get(i).getMetacriticScore())
                )
            )
        );
    }

    @Test
    void loadFromFile_ShouldThrowException_WhenFileNotFound() {
        // Arrange
        String nonExistentFilePath = "non-existent.json";        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            jsonService.loadFromFile(nonExistentFilePath, new TypeReference<List<Game>>() {})
        );
    }
}