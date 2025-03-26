package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.repository.DailyReportRepository;

@Service
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;

    public DailyReportService(DailyReportRepository dailyReportRepository) {
        this.dailyReportRepository = dailyReportRepository;
    }

// 日報一覧表示
    public List<DailyReport> findAll() {
        return dailyReportRepository.findAll();
    }

// 日報保存
    public void save(DailyReport dailyReport) {

        LocalDateTime now = LocalDateTime.now();
        dailyReport.setCreatedAt(now);
        dailyReport.setUpdatedAt(now);

        dailyReportRepository.save(dailyReport);
    }

// employeeとreportDateが存在するかどうかチェック
    // 空かどうかを判定したいので、isEmptyがtrueの時にfalseを返すように実装
    public boolean existsReportByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        return !dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate).isEmpty();
    }

    // 指定した従業員と日付の日報を取得（なければ null を返す）
    public DailyReport findByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        return dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate)
                .stream()
                .findFirst()
                .orElse(null);
    }

// 一件検索
    public DailyReport findById(Integer id) {
        Optional<DailyReport> option = dailyReportRepository.findById(id);
        DailyReport dailyReport = option.orElse(null);
        return dailyReport;
    }

// 日報削除
    public void delete(Integer id) {
        DailyReport dailyReport = findById(id);
        LocalDateTime now = LocalDateTime.now();
        dailyReport.setUpdatedAt(now);
        dailyReport.setDeleteFlg(true);
        dailyReportRepository.save(dailyReport);
    }

// 更新処理
    @Transactional
    public void update(DailyReport dailyReportCurrentData, DailyReport dailyReport) {
        dailyReportCurrentData.setContent(dailyReport.getContent());
        dailyReportCurrentData.setTitle(dailyReport.getTitle());
        dailyReportCurrentData.setReportDate(dailyReport.getReportDate());
        dailyReportCurrentData.setUpdatedAt(LocalDateTime.now());
        dailyReportRepository.save(dailyReportCurrentData);
    }
}
