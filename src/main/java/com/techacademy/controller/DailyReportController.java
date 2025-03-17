package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.repository.EmployeeRepository;
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("reports")
public class DailyReportController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    @Autowired
    public DailyReportController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

//    日報一覧画面
    @GetMapping
    public String list(Model model) {
        return "dailyReport/dailyReportList";
    }

}
