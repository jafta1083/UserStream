package com.userstream.web;

import com.userstream.common.ApiResponse;
import com.userstream.user.UserData;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public void registerRoutes(Javalin app) {
        app.get("/api/users", this::getAllUsers);
        app.post("/api/users", this::createUser);
    }

    private void getAllUsers(Context ctx) {
        ctx.json(ApiResponse.success("Users list"));
    }

    private void createUser(Context ctx) {
        UserData user = ctx.bodyAsClass(UserData.class);

        if (user.getName() == null || user.getEmail() == null) {
            ctx.status(400).json(ApiResponse.error("Name and email are required"));
            return;
        }

        ctx.status(201).json(ApiResponse.success("User created successfully", user));
    }
}
