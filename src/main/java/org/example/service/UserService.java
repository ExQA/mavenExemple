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
        // Перевірка унікальності псевдоніму
        if (userRepository.isNicknameUnique(nickname)) {
            User newUser = User.builder()
                    .name(name)
                    .nickname(nickname)
                    .birthday(birthday)
                    .password(password)
                    .build();

            // Збереження користувача
            return userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException("Псевдонім вже використовується");
        }
    }

    public User login(String nickname, String password) {
        // Перевірка користувача та пароля
        User user = userRepository.findByNicknameAndPassword(nickname, password);

        if (user != null) {
            // Успішний вхід
            return user;
        } else {
            // Помилка
            throw new IllegalArgumentException("Неправильний користувач або пароль");
        }
    }
}