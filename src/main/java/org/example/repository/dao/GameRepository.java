package org.example.repository.dao;

import org.example.model.Game;

import java.util.List;

public interface GameRepository {
    Game save(Game game);

    Game get(int id);

    boolean remove(int id);

    int update(Game game);

    List<Game> findAll();

    List<Game> findAllUserGames(int userId);

    Game findByName(String gameName);

    boolean buyGame(int userId, int gameId);
}
