package com.userstream.web;

import io.javalin.Javalin;

import java.util.HashMap;

public class App {
    
    public static void main(String[] args) {

        // Initialize controllers
        UserController userController = new UserController();
        ReportController reportController = new ReportController();
        
        // Create and configure Javalin app
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7070);
        
        System.out.println("UserStream API started on http://localhost:7070");
        
        // Register routes
        userController.registerRoutes(app);
        reportController.registerRoutes(app);
        
        // Health check endpoint
        app.get("/health", ctx -> {
            HashMap<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("service", "UserStream");
            ctx.json(health);
        });
    }
}
