package org.example.service;

import org.example.model.Game;
import org.example.model.User;
import org.example.repository.dao.UserRepository;

import java.sql.Date;

public class UserService {
    private final UserRepository userRepository;
    private final GameService gameService;

    public UserService(UserRepository userRepository, GameService gameService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
    }

    public User getByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User creditUser(User user, double amount) {
        user.setAmount(user.getAmount() + amount);
        userRepository.update(user);
        return user;
    }

    public User buyGame(User user, Game game) {
        user.setAmount(user.getAmount() - game.getCost());
        userRepository.update(user);
        user.getGames().add(game);
        gameService.buyGame(user.getId(), game.getId());
        return user;
    }

    public User registerNewUser(String name, String nickname, Date birthday, String password) {
        if (userRepository.isNicknameUnique(nickname)) {
            User newUser = User.builder()
                    .name(name)
                    .nickname(nickname)
                    .birthday(birthday)
                    .password(password)
                    .amount(0)
                    .build();

            return userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException("Nickname is already used");
        }
    }

    public User login(String nickname, String password) {
        User user = userRepository.findByNicknameAndPassword(nickname, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user or password");
        }
        user.setGames(gameService.findAllUserGames(user.getId()));
        return user;
    }
}
