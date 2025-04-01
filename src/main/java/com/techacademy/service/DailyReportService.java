package com.techacademy.service;

import java.security.Principal;
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

    private final EmployeeService employeeService;

    private final DailyReportRepository dailyReportRepository;

    public DailyReportService(DailyReportRepository dailyReportRepository, EmployeeService employeeService) {
        this.dailyReportRepository = dailyReportRepository;
        this.employeeService = employeeService;
    }

    // 日報一覧表示
    public List<DailyReport> findAll() {
        return dailyReportRepository.findAll();
    }

    // 指定した従業員の日報のみを表示
    public List<DailyReport> findByEmployee(Employee employee) {
        return dailyReportRepository.findByEmployee(employee);
    }

    // ログイン中のユーザ情報の取得
    public Employee getLoginUser(Principal principal) {
        // getNameにより社員番号を取得
        String code = principal.getName();
        // 社員番号を参照し、ユーザ情報(レコード)を返す
        return employeeService.findByCode(code);
    }

    // 日報保存
    public void save(DailyReport dailyReport) {

        LocalDateTime now = LocalDateTime.now();
        dailyReport.setCreatedAt(now);
        dailyReport.setUpdatedAt(now);

        dailyReportRepository.save(dailyReport);
    }

    //指定したユーザと日付のデータが存在するかどうかチェック
    public boolean existsReportByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        //指定したデータが存在する場合、trueを返す
        return !dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate).isEmpty();
    }

    // ログイン中のユーザ かつ 入力された日付が同じデータを取得(該当データが存在しなければnullを返す)
    public DailyReport findByEmployeeAndDate(Employee employee, LocalDate reportDate) {
        return dailyReportRepository.findByEmployeeAndReportDate(employee, reportDate)
                // streamに変換
                .stream()
                // 最初の要素を取得
                .findFirst()
                // Optionalの中身があればその値を返す なければnullを返す
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
