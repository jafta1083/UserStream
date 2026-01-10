package com.userstream.notification;

import io.javalin.Javalin;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {

    private static final int DEFAULT_PORT = 7004;
    private Javalin server;

    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();
        notificationService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("NotificationService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("NotificationService stopped");
        }
    }

    private Javalin initHttpServer() {
        NotificationRepository repository = new NotificationRepository();

        Javalin app = Javalin.create();

        // Sends notification messages
        app.get("/notifications", ctx -> {
            List<Notification> notifications = repository.findAll();
            ctx.json(notifications);
        });

        app.post("/notifications", ctx -> {
            Notification notification = ctx.bodyAsClass(Notification.class);
            repository.save(notification);
            ctx.status(201).json(notification);
        });

        app.get("/notifications/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Notification not found")
            );
        });

        app.post("/notifications/{id}/send", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    notification -> {
                        notification.setStatus("SENT");
                        notification.setSentAt(LocalDateTime.now());
                        repository.save(notification);
                        ctx.json(notification);
                    },
                    () -> ctx.status(404).result("Notification not found")
            );
        });

        return app;
    }
}
