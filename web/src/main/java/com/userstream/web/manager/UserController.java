package com.userstream.web.manager;

import com.userstream.common.ApiResponse;
import com.userstream.user.UserData;
import com.userstream.web.service.CsvUserWriter;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserController {

    private final CsvUserWriter csvWriter;

    public UserController() {
        this.csvWriter = new CsvUserWriter();
    }

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

        // Set user properties
        user.setId(Integer.parseInt(UUID.randomUUID().toString()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        // Write user to CSV file
        try {
            csvWriter.writeUser(user);
            System.out.println("User written to CSV: " + csvWriter.getCsvFilePath());
        } catch (IOException e) {
            System.err.println("Failed to write user to CSV: " + e.getMessage());
            ctx.status(500).json(ApiResponse.error("Failed to save user to CSV: " + e.getMessage()));
            return;
        }

        ctx.status(201).json(ApiResponse.success("User created successfully", user));
    }
}
