package com.userstream.events;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    void save(Event event);

    Optional<Event> findById(int id);

    List<Event> findAll();

    void delete(int id);
}
