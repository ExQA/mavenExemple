package org.example.repository;

import org.example.model.Game;
import org.example.repository.dao.GameRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRepositoryImpl implements GameRepository {
    private final Connection connection;

    private static final String SAVE = "INSERT INTO games(name, release_date, rating, cost, description) VALUES (?, ?, ?, ?, ?)";
    private static final String GET = "SELECT * FROM games WHERE id = ?";
    private static final String REMOVE = "DELETE FROM games WHERE id = ?";
    private static final String UPDATE = "UPDATE games SET name=?, release_date=?, rating=?, cost=?, description=? WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM games";

    public GameRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Game save(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setFloat(3, game.getRating());
            preparedStatement.setFloat(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                game.setId(generatedKeys.getInt(1));
            }

            return game;
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public Game get(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToGame(resultSet);
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
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
            preparedStatement.setFloat(4, game.getCost());
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
        List<Game> games = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                games.add(mapResultSetToGame(resultSet));
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return games;
    }

    private Game mapResultSetToGame(ResultSet resultSet) throws SQLException {
        return Game.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date"))
                .rating(resultSet.getFloat("rating"))
                .cost(resultSet.getFloat("cost"))
                .description(resultSet.getString("description"))
                .build();
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