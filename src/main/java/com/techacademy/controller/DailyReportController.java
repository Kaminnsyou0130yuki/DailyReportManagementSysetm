package com.techacademy.controller;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.repository.DailyReportRepository;
import com.techacademy.service.DailyReportService;
import com.techacademy.service.EmployeeService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;
    private final EmployeeService employeeService;

    @Autowired
    DailyReportController(
            DailyReportService dailyReportService,
            EmployeeService employeeService) {

        this.dailyReportService = dailyReportService;
        this.employeeService = employeeService;
    }

// 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", dailyReportService.findAll().size());
        model.addAttribute("dairyReportList", dailyReportService.findAll());
        return "dailyReport/dailyReportList";
    }

// 新規登録画面
    @GetMapping("/add")
    public String create(@ModelAttribute DailyReport dailyReport, Principal principal, Model model) {
        // ログイン中のユーザの社員番号取得
        String code = principal.getName();
        // ログイン中のユーザの社員番号を使用してレコード取得
        Employee employee = employeeService.findByCode(code);
        // dailyReportエンティティのemployeeにセット
        dailyReport.setEmployee(employee);
        return "dailyReport/dailyReportNew";
    }

// 新規登録処理
    @PostMapping("/add")
    public String add(@ModelAttribute @Validated DailyReport dailyReport, BindingResult res, Principal principal,
            Model model) {

        // エラーがある場合、新規登録画面へ画面遷移
        if (res.hasErrors()) {
            return create(dailyReport, principal, model);
        }

        // login中のユーザ情報を取得
        // getNameで社員番号を取得
        String code = principal.getName();
        // employeeに社員番号で検索したレコード（ログイン中の従業員の情報）を格納
        Employee employee = employeeService.findByCode(code);

        // 日報テーブルに ログイン中のユーザかつ入力した日付 の日報データが存在する場合エラー
        // ErrorMessagesクラスに専用のエラーメッセージあり
        if (dailyReportService.existsReportByEmployeeAndDate(employee, dailyReport.getReportDate())) {
            // エラー名、エラーメッセージ取得
            String errorName = ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR);
            String errorValue = ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR);
            model.addAttribute(errorName, errorValue);
            return create(dailyReport, principal, model);
        }

        // dailyReportのemployeeにそのレコード(ログイン中の従業員の情報)を格納
        dailyReport.setEmployee(employee);
        // DBにデータを保存
        dailyReportService.save(dailyReport);
        return "redirect:/reports";
    }

// 日報詳細画面
    @GetMapping("/{id}/")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("dailyReport", dailyReportService.findById(id));
        return "dailyReport/dailyReportDetail";
    }

//  日報削除処理
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model) {
        dailyReportService.delete(id);
        return "redirect:/reports";
    }


}
