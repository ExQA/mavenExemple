package org.example.repository;

import org.example.model.Game;
import org.example.repository.dao.GameRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class GameRepositoryImpl implements GameRepository {
    private final Connection connection;

    private static final String SELECT_ALL = "SELECT * FROM games WHERE id = ?";
    private static final String SAVE =
            """
                            INSERT INTO public.games(
                            name, release_date, rating, cost, description)
                            VALUES (?, ?, ?, ?, ?)
                    """;

    private static final String REMOVE =
            """
                            DELETE FROM public.games
                            WHERE id = ?                                
                    """;

    private static final String UPDATE =
            """
                            UPDATE public.games
                            SET name=?, release_date=?, rating=?, cost=?, description=?
                            WHERE id = ?;
                    """;

    public GameRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Game get(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return Game.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .releaseDate(resultSet.getDate("release_date"))
                    .rating(resultSet.getFloat("rating"))
                    .cost(resultSet.getFloat("cost"))
                    .description(resultSet.getString("description"))
                    .build();
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public Game save(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setFloat(3, game.getRating());
            preparedStatement.setDouble(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            game.setId(generatedKeys.getInt(1));
            return game;
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    @Override
    public int update(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setFloat(3, game.getRating());
            preparedStatement.setDouble(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());
            preparedStatement.setInt(6, game.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        }
    }

    @Override
    public List<Game> findAll() {
        return new LinkedList<>();
    }

    private void handleException(SQLException e) {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        throw new RuntimeException(e);
    }
}
