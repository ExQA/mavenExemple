package org.example.repository.dao;

import org.example.model.User;

import java.util.List;

public interface UserRepository {

    User get(int id);

    User save(User user);

    boolean remove(int id);

    int update(User user);

    boolean isNicknameUnique(String nickname);

    User findByNicknameAndPassword(String nickname, String password);

    List<User> findAll();
}
