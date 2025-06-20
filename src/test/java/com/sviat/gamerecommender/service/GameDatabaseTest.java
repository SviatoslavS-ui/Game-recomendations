package com.sviat.gamerecommender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.sviat.gamerecommender.model.Game;

public class GameDatabaseTest extends BaseServiceTest {
    
    @Test
    void testAddGame() {
        // Arrange
        Game game = TestGameData.DATABASE_TEST;
        // Act
        gameDatabase.addGame(game);
        // Assert
        assertTrue(gameDatabase.getAllGames().contains(game));
    }

    @Test
    void testUpdateGame() {
        // Arrange
        Game game = TestGameData.DATABASE_TEST_ORIGINAL;
        // Act
        gameDatabase.addGame(game);
        game.updateField("title", "Updated Title");
        gameDatabase.updateGame(game);
        // Assert
        Game result = gameDatabase.getAllGames().stream()
                .filter(g -> g.getId().equals(game.getId())).findFirst().orElseThrow();
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeleteGame() {
        // Arrange
        Game game = TestGameData.DATABASE_TEST;
        // Act
        gameDatabase.addGame(game);
        gameDatabase.deleteGame(game);
        // Assert
        assertTrue(gameDatabase.getAllGames().isEmpty());
    }
}