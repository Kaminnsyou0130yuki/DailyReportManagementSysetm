package com.techacademy.repository;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import java.util.List;


public interface DailyReportRepository extends JpaRepository<DailyReport, Integer> {

//      findByは検索用メソッドのプレフィックス。データベースからemployee及び日付を取得している。
    List<DailyReport> findByEmployeeAndReportDate(Employee employee, LocalDate reportDate);

}
