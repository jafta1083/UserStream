package com.userstream.web.service;

import com.userstream.web.request.WebRequest;
import com.userstream.web.request.WebRequestRepository;
import io.javalin.Javalin;

import java.util.List;

public class WebService {

    private static final int DEFAULT_PORT = 7000;
    private Javalin server;

    public static void main(String[] args) {
        WebService webService = new WebService();
        webService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("WebService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("WebService stopped");
        }
    }

    private Javalin initHttpServer() {
        WebRequestRepository repository = new com.userstream.web.request.InMemoryWebRequestRepository();

        Javalin app = Javalin.create();

        // GET all requests
        app.get("/requests", ctx -> {
            List<WebRequest> requests = repository.findAll();
            ctx.json(requests);
        });

        // GET request by ID
        app.get("/requests/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Request not found")
            );
        });

        // POST create request
        app.post("/requests", ctx -> {
            WebRequest request = ctx.bodyAsClass(WebRequest.class);
            repository.save(request);
            ctx.status(201).json(request);
        });

        // DELETE request
        app.delete("/requests/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.delete(id);
            ctx.status(204);
        });

        return app;
    }
}
