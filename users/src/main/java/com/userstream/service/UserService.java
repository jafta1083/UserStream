package com.userstream.service;

import com.userstream.db.DatabaseConfig;
import com.userstream.repository.InMemoryUserRepository;
import com.userstream.user.UserData;
import io.javalin.Javalin;

import java.util.List;
import java.util.logging.Logger;

public class UserService {

    private static Logger logger = Logger.getLogger("");
    private static final int DEFAULT_PORT = 7001;
    private Javalin server;

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("UserService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("UserService stopped");
        }
    }

    private Javalin initHttpServer() {
        InMemoryUserRepository repository = new InMemoryUserRepository();

        Javalin app = Javalin.create();

        // GET all users
        app.get("/users", ctx -> {
            List<UserData> users = repository.findAll();
            ctx.json(users);
        });

        // GET user by ID
        app.get("/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("User not found")
            );
        });

        // POST create user
        app.post("/users", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            repository.save(user);
            ctx.status(201).json(user);
        });

        // PUT update user
        app.put("/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            UserData updatedUser = ctx.bodyAsClass(UserData.class);
            updatedUser.setId(id);
            repository.save(updatedUser);
            ctx.json(updatedUser);
        });

        // DELETE user
        app.delete("/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = repository.deleteById(id);
            if (deleted) {
                ctx.status(204);
            } else {
                ctx.status(404).result("User not found");
            }
        });

        return app;
    }
}
