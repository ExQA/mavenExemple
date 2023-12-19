package org.example.repository.dao;

import org.example.model.Game;

import java.util.List;

public interface GameRepository {
    Game save(Game game);

    Game get(int id);

    List<Game> findAll();
}
