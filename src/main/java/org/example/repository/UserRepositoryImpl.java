package org.example.repository;

import org.example.model.User;
import org.example.repository.dao.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    private static final String SELECT_ALL = "SELECT * FROM users WHERE id = ?";
    private static final String SAVE =
            """
                            INSERT INTO users(
                            name, nickname, birthday, password, amount)
                            VALUES (?, ?, ?, ?, ?)
                    """;

    private static final String REMOVE =
            """
                                    DELETE FROM users
                                    WHERE id = ?                                
                    """;

    private static final String UPDATE =
            """
                            UPDATE users
                            SET name=?, nickname=?, birthday=?, password=?, amount=?
                            WHERE id = ?;
                    """;
    private static final String FIND_BY_NICKNAME_AND_PASSWORD = "SELECT * FROM users WHERE nickname = ? AND password = ?";
    private static final String FIND_BY_NICKNAME = "SELECT * FROM users WHERE nickname = ?";
    private static final String IS_NICKNAME_UNIQUE = "SELECT COUNT(*) FROM users WHERE nickname = ?";


    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User get(int id) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_ALL);) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return mapResultSetToUser(resultSet);
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public User save(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDouble(5, user.getAmount());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getInt(1));
            return user;
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public boolean remove(int id) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(REMOVE)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    @Override
    public int update(User user) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDouble(5, user.getAmount());

            preparedStatement.setInt(6, user.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        }
    }

    @Override
    public boolean isNicknameUnique(String nickname) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(IS_NICKNAME_UNIQUE)) {
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count == 0;
        } catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    @Override
    public User findByNicknameAndPassword(String nickname, String password) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(FIND_BY_NICKNAME_AND_PASSWORD)) {
            preparedStatement.setString(1, nickname);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    @Override
    public User findByNickname(String nickname) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(FIND_BY_NICKNAME)) {
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new LinkedList<>();
    }

    private static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .nickname(resultSet.getString("nickname"))
                .birthday(resultSet.getDate("birthday"))
                .password(resultSet.getString("password"))
                .amount(resultSet.getDouble("amount"))
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