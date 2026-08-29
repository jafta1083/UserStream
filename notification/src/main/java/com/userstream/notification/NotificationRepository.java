package com.userstream.notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void save(Notification notification);

    Optional<Notification> findById(int id);

    List<Notification> findAll();

    List<Notification> findByUserId(int userId);

    void delete(int id);
}
