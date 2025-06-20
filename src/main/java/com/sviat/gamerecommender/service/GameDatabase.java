package com.sviat.gamerecommender.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sviat.gamerecommender.model.Game;

public class GameDatabase {
    private List<Game> games;
    private final JsonService jsonService;

    public GameDatabase(JsonService jsonService) {
        this.games = new ArrayList<>();
        this.jsonService = jsonService;
    }
    
    // Data loading and saving
    public void loadGamesFromFile(String filePath) {
        games = jsonService.loadFromFile(filePath, new TypeReference<List<Game>>() {});
    }

    public void saveGamesToFile(String filePath) {
        jsonService.saveToFile(filePath, games);
    }

    // CRUD operations
    public void addGame(Game game) {
        games.add(game);
    }

    public void updateGame(Game game) {
        int index = games.indexOf(game);
        if (index != -1) {
            games.set(index, game);
        }
    }

    public void deleteGame(Game game) {
        games.remove(game);
    }

    public List<Game> getAllGames() {
        return games;
    }
}