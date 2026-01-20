package com.userstream.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements NotificationRepository{

    private final ConcurrentHashMap<Integer, Notification> notifications = new ConcurrentHashMap<>();

    @Override
    public void save(Notification notification) {
        notifications.put(notification.getId(),notification);
    }

    @Override
    public Optional<Notification> findById(int id) {
        return Optional.ofNullable(notifications.get(id));
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notifications.values());
    }

    @Override
    public List<Notification> findByUserId(int userId) {
        return notifications.values().stream()
                .filter(ntf -> ntf.getUserId() == userId)
                .toList();
    }

    @Override
    public void delete(int id) {
        notifications.remove(id);

    }
}
