package com.userstream.web.manager;

import com.userstream.repository.UserRepository;
import com.userstream.user.UserData;
import io.javalin.http.Context;

import java.util.UUID;

public class UserHandler {

    private final UserRepository repository;

    public UserHandler(UserRepository repository) {
        this.repository = repository;
    }

    // POST /users
    public void createUser(Context ctx) {
        UserData request = ctx.bodyAsClass(UserData.class);

        request.setId(Integer.parseInt(UUID.randomUUID().toString()));
        repository.save(request);

        ctx.status(201);
        ctx.json(request);
    }

    // GET /users
    public void getAllUsers(Context ctx) {
        ctx.json(repository.findAll());
    }

    // GET /users/{id}
    public void getUserById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        repository.findById(id)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("User not found")
                );
    }
}
