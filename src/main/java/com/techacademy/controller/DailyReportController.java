package com.techacademy.controller;

import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.repository.DailyReportRepository;
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

    private final EmployeeController employeeController;
    private final DailyReportRepository dailyReportRepository;
    private final DailyReportService dailyReportService;
    private final EmployeeService employeeService;

    @Autowired
    DailyReportController(DailyReportService dailyReportService, EmployeeService employeeService,
            DailyReportRepository dailyReportRepository, UserDetailService userDetailService,
            EmployeeController employeeController) {

        this.dailyReportService = dailyReportService;
        this.employeeService = employeeService;
        this.dailyReportRepository = dailyReportRepository;
        this.employeeController = employeeController;

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
    public String create(Principal principal, Model model) {
        String code = principal.getName();
        model.addAttribute("employee", employeeService.findByCode(code));
        return "dailyReport/dailyReportNew";
    }

//    新規登録処理
//    入力チェックも実装しなきゃいけない。画面設計者に入力チェック仕様が記載されている
    @PostMapping("/add")
    public String add(@ModelAttribute DailyReport dailyReport, Principal principal, Model model) {
        String code = principal.getName();
        Employee employeeCode = employeeService.findByCode(code);
        dailyReport.setEmployee(employeeCode);
//        saveメソッドを作成する　EmployeeServiceクラスを参考にする
        dailyReportService.save(dailyReport);
        return "redirect:/reports";
    }
}
