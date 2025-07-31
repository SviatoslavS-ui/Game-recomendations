package com.sviat.gamerecommender.controller;

import com.sviat.gamerecommender.model.Game;
import com.sviat.gamerecommender.service.GameDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameDatabase gameDatabase;

    @Autowired
    public GameController(GameDatabase gameDatabase) {
        this.gameDatabase = gameDatabase;
    }

    @GetMapping
    public String browseGames(Model model) {
        // Get all games for the browse page
        List<Game> allGames = gameDatabase.getAllGames()
        .stream()
        .sorted(Comparator.comparing(Game::getTitle, String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
        
        // Add data to the model
        model.addAttribute("allGames", allGames);
        model.addAttribute("totalGames", allGames.size());
        
        return "pages/games";
    }
}
