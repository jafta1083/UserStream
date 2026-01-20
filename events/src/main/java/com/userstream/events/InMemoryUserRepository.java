package com.userstream.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements EventRepository{

    private final ConcurrentHashMap<Integer, Event> events = new ConcurrentHashMap<>();

    @Override
    public void save(Event event) {
        events.put(event.getId(),event);

    }

    @Override
    public Optional<Event> findById(int id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }

    @Override
    public void delete(int id) {
        events.remove(id);

    }
}
