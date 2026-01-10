package com.userstream.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryAlertRepository implements AlertRepository {
    private final Map<String, Alert> alerts = new ConcurrentHashMap<>();

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
    public List<Alert> findByUserId(String userId) {
        return alerts.values().stream()
                .filter(alert -> userId.equals(alert.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findUnreadByUserId(String userId) {
        return alerts.values().stream()
                .filter(alert -> userId.equals(alert.getUserId()) && !alert.isRead())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        alerts.remove(id);
    }

    @Override
    public List<Alert> findAll() {
        return new ArrayList<>(alerts.values());
    }
}
