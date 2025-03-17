package com.techacademy.service;


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

//    入力チェックとかもここでやるって感じかな？？

}