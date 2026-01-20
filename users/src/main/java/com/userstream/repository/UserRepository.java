package com.userstream.repository;

import com.userstream.user.UserData;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserData save(UserData user);

    Optional<UserData> findById(int id);

    Optional<UserData> findByUsername(String username);

    Optional<UserData> findByEmail(String email);

    List<UserData> findAll();

    boolean deleteById(int id);
}
