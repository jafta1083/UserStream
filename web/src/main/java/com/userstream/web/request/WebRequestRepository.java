package com.userstream.web.request;

import java.util.List;
import java.util.Optional;

public interface WebRequestRepository {
    void save(WebRequest request);

    Optional<WebRequest> findById(String id);

    List<WebRequest> findAll();

    void delete(String id);
}
