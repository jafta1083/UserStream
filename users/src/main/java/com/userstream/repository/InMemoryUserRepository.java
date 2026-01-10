package com.userstream.repository;

import com.userstream.user.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, UserData> users = new ConcurrentHashMap<>();

    @Override
    public UserData save(UserData user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<UserData> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<UserData> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> username.equals(user.getName()))
                .findFirst();
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Override
    public List<UserData> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean deleteById(String id) {
        return users.remove(id) != null;
    }
}
