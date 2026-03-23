package com.userstream.alert;

import io.javalin.Javalin;

import java.util.List;

public class AlertService {

    private static final int DEFAULT_PORT = 7005;
    private final InMemoryAlertRepository repository = new InMemoryAlertRepository();
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

        Javalin app = Javalin.create();

//        undone checking the UnreadUserID
// GET unread alerts by user ID
    app.get("/alerts/user/{userId}/unread", ctx -> {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        List<Alert> unreadAlerts = repository.findByUserId(userId).stream()
            .filter(alert -> !alert.isRead())
            .toList();
        ctx.json(unreadAlerts);
    });

    // POST mark all alerts as read for a user
    app.post("/alerts/user/{userId}/read", ctx -> {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        List<Alert> userAlerts = repository.findByUserId(userId);
        userAlerts.forEach(alert -> {
        alert.setRead(true);
        repository.save(alert);
        });
        ctx.json(userAlerts);
    });

        // GET all alerts
        app.get("/alerts", ctx -> {
            List<Alert> alerts = repository.findAll();

            ctx.json(alerts);
        });

        // GET alert by ID
        app.get("/alerts/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

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
            int userId = Integer.parseInt(ctx.pathParam("userId"));

            List<Alert> alerts = repository.findByUserId(userId);
            ctx.json(alerts);
        });

        // POST mark alert as read
        app.post("/alerts/{id}/read", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

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
            int id = Integer.parseInt(ctx.pathParam("id"));

            repository.deleteById(id);
            ctx.status(204);
        });

        return app;
    }
}
