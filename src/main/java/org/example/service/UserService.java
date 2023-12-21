package org.example.service;

import org.example.model.User;
import org.example.repository.dao.UserRepository;

import java.sql.Date;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUser(String name, String nickname, Date birthday, String password) {
        // Checking unique user
        if (userRepository.isNicknameUnique(nickname)) {
            User newUser = User.builder()
                    .name(name)
                    .nickname(nickname)
                    .birthday(birthday)
                    .password(password)
                    .build();

            // Saving the user
            return userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException("Nickname is already in use");
        }
    }

    public User login(String nickname, String password) {
        // ПChecking user and password
        User user = userRepository.findByNicknameAndPassword(nickname, password);

        if (user != null) {
            // Successful login
            return user;
        } else {
            // Error
            throw new IllegalArgumentException("Wring user or password");
        }
    }
}
