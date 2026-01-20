package com.userstream.reports;

import com.userstream.service.UserReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements ReportRepository{

    private final ConcurrentHashMap<Integer, Report> reports = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, UserReport> userReports = new ConcurrentHashMap<>();

    @Override
    public void save(Report report) {
        reports.put(report.getId(), report);

    }

    @Override
    public void saveUserReport(UserReport userReport) {
        userReports.put(userReport.getId(), userReport);

    }

    public Optional<Report> findById(int id) {
        return Optional.ofNullable(reports.get(id));
    }

    public List<Report> findAll() {
        return new ArrayList<>(reports.values());
    }

    @Override
    public List<UserReport> findAllUserReports() {
        return new ArrayList<>(userReports.values());
    }

    @Override
    public void delete(int id) {
        reports.remove(id);

    }

    @Override
    public String generateCSV(List<String[]> data) {
        return "";
    }

    @Override
    public String generateUserReportCSV() {
        return "";
    }
}
