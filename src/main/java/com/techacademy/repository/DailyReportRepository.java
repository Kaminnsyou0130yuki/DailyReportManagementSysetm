package com.techacademy.repository;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import java.util.List;
import java.util.Optional;


public interface DailyReportRepository extends JpaRepository<DailyReport, Integer> {


    List<DailyReport> findByEmployeeAndReportDate(Employee employee, LocalDate reportDate);

}
