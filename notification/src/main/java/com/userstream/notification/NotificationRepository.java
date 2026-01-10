package com.userstream.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationRepository {
    private final ConcurrentHashMap<String, Notification> notifications = new ConcurrentHashMap<>();

    public void save(Notification notification) {
        notifications.put(notification.getId(), notification);
    }

    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(notifications.get(id));
    }

    public List<Notification> findAll() {
        return new ArrayList<>(notifications.values());
    }

    public List<Notification> findByUserId(String userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .toList();
    }

    public void delete(String id) {
        notifications.remove(id);
    }
}
