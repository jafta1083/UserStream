package com.userstream.events;

import java.util.List;
import java.util.Optional;

public class Memory_DB implements EventRepository{
    @Override
    public void save(Event event) {

    }

    @Override
    public Optional<Event> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Event> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }
}
