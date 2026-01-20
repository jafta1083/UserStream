package com.userstream.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.userstream.web.manager.ReportController;
import com.userstream.web.manager.UserController;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.util.HashMap;

public class App {
    
    public static void main(String[] args) {

        // Initialize controllers
        UserController userController = new UserController();
        ReportController reportController = new ReportController();
        
        // Configure ObjectMapper with Java 8 date/time support
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Create and configure Javalin app
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
            config.jsonMapper(new JavalinJackson(objectMapper));
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
