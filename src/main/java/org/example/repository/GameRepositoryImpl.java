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
    private static final String FIND_GAME = "SELECT * FROM games where name=?";
    private static final String BUY_GAME = "INSERT INTO usergames(user_id, game_id) VALUES (?, ?)";
    private static final String FIND_ALL_USER_GAMES = "SELECT * FROM games where id in (select game_id from usergames where user_id = ?)";

    public GameRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Game save(Game game) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setDouble(3, game.getRating());
            preparedStatement.setDouble(4, game.getCost());
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
    public boolean buyGame(int userId, int gameId) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(BUY_GAME)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, gameId);

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    @Override
    public Game get(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET)) {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.execute();
        } catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    @Override
    public int update(Game game) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, game.getName());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setDouble(3, game.getRating());
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
        List<Game> games = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                games.add(mapResultSetToGame(resultSet));
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return games;
    }

    @Override
    public List<Game> findAllUserGames(int userId) {
        List<Game> games = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USER_GAMES)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                games.add(mapResultSetToGame(resultSet));
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return games;
    }

    @Override
    public Game findByName(String gameName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_GAME)) {
            preparedStatement.setString(1, gameName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToGame(resultSet);
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    private Game mapResultSetToGame(ResultSet resultSet) throws SQLException {
        return Game.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date"))
                .rating(resultSet.getDouble("rating"))
                .cost(resultSet.getDouble("cost"))
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