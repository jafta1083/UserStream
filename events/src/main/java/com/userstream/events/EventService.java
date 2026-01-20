package com.userstream.events;

import io.javalin.Javalin;

import java.util.List;

public class EventService {

    private static final int DEFAULT_PORT = 7002;
    private Javalin server;
    private InMemoryUserRepository repository;

    public static void main(String[] args) {
        EventService eventService = new EventService();
        eventService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("EventService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("EventService stopped");
        }
    }

    private Javalin initHttpServer() {

        Javalin app = Javalin.create();

        // Receives events from queue
        app.get("/events", ctx -> {
            List<Event> events = repository.findAll();
            ctx.json(events);
        });

        app.post("/events", ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
            repository.save(event);
            ctx.status(201).json(event);
        });

        app.get("/events/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            
            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Event not found")
            );
        });

        return app;
    }
}
