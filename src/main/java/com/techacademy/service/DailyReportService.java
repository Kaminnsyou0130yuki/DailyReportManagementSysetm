package com.techacademy.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.repository.DailyReportRepository;

@Service
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;

    public DailyReportService(DailyReportRepository dailyReportRepository) {
        this.dailyReportRepository = dailyReportRepository;
    }

//    日報一覧表示
    public List<DailyReport> findAll() {
        return dailyReportRepository.findAll();
    }

//    日報保存
    public void save(DailyReport dailyReport) {

        LocalDateTime now = LocalDateTime.now();
        dailyReport.setCreatedAt(now);
        dailyReport.setUpdatedAt(now);

        dailyReportRepository.save(dailyReport);
    }

    public boolean existsReportByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        return !dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate).isEmpty();
    }


    public DailyReport getReportByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        List<DailyReport> reports = dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate);

        if (reports.isEmpty()) {
            return null; // データなし
        } else {
            return reports.get(0); // 最初の1件だけ返す
        }
    }
}