package com.userstream.alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    Alert save(Alert alert);

    Optional<Alert> findById(String id);

    List<Alert> findByUserId(String userId);

    List<Alert> findUnreadByUserId(String userId);

    void deleteById(String id);

    List<Alert> findAll();
}
