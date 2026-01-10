package com.userstream.service;

import com.userstream.reports.Report;
import com.userstream.reports.ReportsDB;
import io.javalin.Javalin;

import java.util.List;

public class ReportService {

    private static final int DEFAULT_PORT = 7003;
    private Javalin server;
    private ReportsDB repository;

    public static void main(String[] args) {
        ReportService reportService = new ReportService();
        reportService.start(DEFAULT_PORT);
    }

    public void start(int port) {
        server = initHttpServer();
        server.start(port);
        System.out.println("ReportService started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("ReportService stopped");
        }
    }

    private Javalin initHttpServer() {

        Javalin app = Javalin.create();

        // GET all reports
        app.get("/reports", ctx -> {
            List<Report> reports = repository.findAll();
            ctx.json(reports);
        });

        // GET report by ID
        app.get("/reports/{id}", ctx -> {
            String id = ctx.pathParam("id");
            repository.findById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Report not found")
            );
        });

        // POST create report
        app.post("/reports", ctx -> {
            Report report = ctx.bodyAsClass(Report.class);
            repository.save(report);
            ctx.status(201).json(report);
        });

        // POST user data for storage and reporting
        app.post("/reports/users", ctx -> {
            UserReport userReport = ctx.bodyAsClass(UserReport.class);
            repository.saveUserReport(userReport);
            ctx.status(201).json(userReport);
        });

        // GET all user reports
        app.get("/reports/users", ctx -> {
            List<UserReport> userReports = repository.findAllUserReports();
            ctx.json(userReports);
        });

        // GET user reports as CSV
        app.get("/reports/users/csv", ctx -> {
            String csv = repository.generateUserReportCSV();
            ctx.contentType("text/csv")
               .header("Content-Disposition", "attachment; filename=\"user-report.csv\"")
               .result(csv);
        });

        // POST generate custom CSV from data
        app.post("/reports/generate-csv", ctx -> {
            List<String[]> data = ctx.bodyAsClass(List.class);
            String csv = repository.generateCSV(data);
            ctx.contentType("text/csv").result(csv);
        });

        return app;
    }
}
