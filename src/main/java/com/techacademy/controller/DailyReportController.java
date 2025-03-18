package com.techacademy.controller;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.service.DailyReportService;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.UserDetailService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;
    private final EmployeeService employeeService;

    @Autowired
    DailyReportController(DailyReportService dailyReportService,
            EmployeeService employeeService,UserDetailService userDetailService) {

        this.dailyReportService = dailyReportService;
        this.employeeService = employeeService;
    }

//    日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", dailyReportService.findAll().size());
        model.addAttribute("dairyReportList", dailyReportService.findAll());
        return "dailyReport/dailyReportList";
    }

//    新規登録画面
    @GetMapping("/add")
    public String create(@ModelAttribute DailyReport dailyReport, Principal principal, Model model) {
        String code = principal.getName();
        Employee employee = employeeService.findByCode(code);
        dailyReport.setEmployee(employee);
        return "dailyReport/dailyReportNew";
    }

//    新規登録処理
//    入力チェックも実装しなきゃいけない。画面設計者に入力チェック仕様が記載されている
    @PostMapping("/add")
    public String add(@ModelAttribute DailyReport dailyReport, Principal principal, Model model) {
        String code = principal.getName();
        Employee employee = employeeService.findByCode(code);
        dailyReport.setEmployee(employee);
        dailyReportService.save(dailyReport);
        return "redirect:/reports";
    }
}
