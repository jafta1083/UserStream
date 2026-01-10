package com.userstream.alert;

import io.javalin.Javalin;

import java.util.List;

public class AlertService {

    private static final int DEFAULT_PORT = 7005;
    private Javalin server;

    public static void main(String[] args) {
        AlertService alertService = new AlertService();
        alertService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("AlertService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("AlertService stopped");
        }
    }

    private Javalin initHttpServer() {
        InMemoryAlertRepository repository = new InMemoryAlertRepository();

        Javalin app = Javalin.create();

        // GET all alerts
        app.get("/alerts", ctx -> {
            List<Alert> alerts = repository.findAll();
            ctx.json(alerts);
        });

        // GET alert by ID
        app.get("/alerts/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Alert not found")
            );
        });

        // POST create alert
        app.post("/alerts", ctx -> {
            Alert alert = ctx.bodyAsClass(Alert.class);
            repository.save(alert);
            ctx.status(201).json(alert);
        });

        // GET alerts by user ID
        app.get("/alerts/user/{userId}", ctx -> {
            String userId = ctx.pathParam("userId");
            List<Alert> alerts = repository.findByUserId(userId);
            ctx.json(alerts);
        });

        // POST mark alert as read
        app.post("/alerts/{id}/read", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    alert -> {
                        alert.setRead(true);
                        repository.save(alert);
                        ctx.json(alert);
                    },
                    () -> ctx.status(404).result("Alert not found")
            );
        });

        // DELETE alert
        app.delete("/alerts/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.deleteById(id);
            ctx.status(204);
        });

        return app;
    }
}
