package org.example.service;

import org.example.model.Game;
import org.example.repository.dao.GameRepository;

import java.sql.Date;
import java.util.List;

public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game addNewGame(String name, Date releaseDate, float rating, float cost, String description) {
        Game newGame = Game.builder()
                .name(name)
                .releaseDate(releaseDate)
                .rating(rating)
                .cost(cost)
                .description(description)
                .build();

        return gameRepository.save(newGame);
    }
}
