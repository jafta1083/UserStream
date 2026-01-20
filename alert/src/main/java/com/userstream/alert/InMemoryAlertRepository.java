package com.userstream.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class InMemoryAlertRepository implements AlertRepository {
    private final Map<Integer, Alert> alerts = new ConcurrentHashMap<>();

    @Override
    public Alert save(Alert alert) {
        alerts.put(alert.getId(), alert);
        return alert;
    }

    @Override
    public Optional<Alert> findById(String id) {
        return Optional.ofNullable(alerts.get(id));
    }

    @Override
    public List<Alert> findByUserId(int userId) {
        return alerts.values().stream()
                .filter(alert -> alert.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findUnreadByUserId(int userId) {
        return alerts.values().stream()
                .filter(alert -> alert.getUserId() == userId && !alert.isRead())
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findAll() {
        return new ArrayList<>(alerts.values());
    }

    @Override
    public void deleteById(int id) {
        alerts.remove(id);
    }
}
