package com.techacademy.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techacademy.entity.DailyReport;
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
//    後で入力チェックの処理追加実装する
    public void save(DailyReport dailyReport) {
        LocalDateTime now = LocalDateTime.now();
        dailyReport.setCreatedAt(now);
        dailyReport.setUpdatedAt(now);

        dailyReportRepository.save(dailyReport);
    }



}