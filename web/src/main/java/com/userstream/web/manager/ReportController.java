package com.userstream.web.manager;

import com.userstream.common.ApiResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

public class ReportController {

    public void registerRoutes(Javalin app) {
        app.get("/api/reports/dashboard", this::getDashboardReport);
        app.get("/api/reports/status", this::getStatusReport);
    }

    private void getDashboardReport(Context ctx) {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("message", "Dashboard report");
        dashboard.put("timestamp", System.currentTimeMillis());
        dashboard.put("services", Map.of(
            "web", "http://localhost:7000",
            "users", "http://localhost:7001",
            "events", "http://localhost:7002",
            "reports", "http://localhost:7003",
            "notifications", "http://localhost:7004"
        ));
        
        ctx.json(ApiResponse.success("Dashboard data retrieved", dashboard));
    }

    private void getStatusReport(Context ctx) {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "WebService");
        status.put("timestamp", System.currentTimeMillis());
        
        ctx.json(ApiResponse.success(status));
    }
}
