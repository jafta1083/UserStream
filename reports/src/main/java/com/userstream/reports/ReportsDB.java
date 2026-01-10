package com.userstream.reports;

import com.userstream.service.UserReport;

import java.util.List;
import java.util.Optional;

public class ReportsDB implements ReportRepository{

    private ReportRepository repository;

    @Override
    public void save(Report report) {

    }

    @Override
    public void saveUserReport(UserReport userReport) {

    }

    @Override
    public Optional<Report> findById(String id) {
        return Optional.empty();
    }

    public List<Report> findAll() {
        return List.of();
    }

    @Override
    public List<UserReport> findAllUserReports() {
        return List.of();
    }

    @Override
    public void delete(String id) {

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
