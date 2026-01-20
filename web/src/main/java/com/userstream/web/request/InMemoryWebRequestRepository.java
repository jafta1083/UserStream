package com.userstream.web.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryWebRequestRepository implements WebRequestRepository {
    private final Map<Integer, WebRequest> requests = new ConcurrentHashMap<>();

    @Override
    public void save(WebRequest request) {
        requests.put(request.getId(), request);
    }

    @Override
    public Optional<WebRequest> findById(int id) {
        return Optional.ofNullable(requests.get(id));
    }

    @Override
    public List<WebRequest> findAll() {
        return new ArrayList<>(requests.values());
    }

    @Override
    public void delete(int id) {
        requests.remove(id);
    }
}
