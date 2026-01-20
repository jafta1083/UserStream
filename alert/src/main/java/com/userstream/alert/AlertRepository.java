package com.userstream.alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {


    Alert save(Alert alert);

    Optional<Alert> findById(int id);

    Optional<Alert> findById(String id);

    List<Alert> findByUserId(int userId);

    List<Alert> findUnreadByUserId(int userId);

    void deleteById(int id);

    List<Alert> findAll();
}
