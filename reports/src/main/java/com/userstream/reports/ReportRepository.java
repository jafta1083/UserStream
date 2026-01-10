package com.userstream.reports;

import com.userstream.service.UserReport;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    void save(Report report);

    void saveUserReport(UserReport userReport);

    Optional<Report> findById(String id);

    List<Report> findAll();

    List<UserReport> findAllUserReports();

    void delete(String id);

    String generateCSV(List<String[]> data);

    String generateUserReportCSV();
}
