package com.techacademy.controller;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.DailyReport;
import com.techacademy.entity.Employee;
import com.techacademy.service.DailyReportService;
import java.security.Principal;
import java.util.Objects;

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

@Controller
@RequestMapping("reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    @Autowired
    DailyReportController(DailyReportService dailyReportService) {

        this.dailyReportService = dailyReportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model, Principal principal) {
        // ログイン中のユーザ情報の取得
        Employee employee = dailyReportService.getLoginUser(principal);

        // ログイン中のユーザが管理者かどうか判定
        boolean existsAdmin = employee.getRole() == Employee.Role.ADMIN;

        if (existsAdmin) {
            // ログイン中のユーザが管理者であれば全社員の日報を表示
            model.addAttribute("listSize", dailyReportService.findAll().size());
            model.addAttribute("dairyReportList", dailyReportService.findAll());
        } else {
            // ログイン中のユーザが一般ユーザであれば自身の日報のみを表示
            model.addAttribute("listSize", dailyReportService.findByEmployee(employee).size());
            model.addAttribute("dairyReportList", dailyReportService.findByEmployee(employee));
        }

        return "dailyReport/dailyReportList";
    }

    // 新規登録画面
    @GetMapping("/add")
    public String create(@ModelAttribute DailyReport dailyReport, Principal principal, Model model) {
        // ログイン中のユーザ情報の取得
        Employee employee = dailyReportService.getLoginUser(principal);
        dailyReport.setEmployee(employee);
        return "dailyReport/dailyReportNew";
    }

    // 新規登録処理
    @PostMapping("/add")
    public String add(@ModelAttribute @Validated DailyReport dailyReport, BindingResult res, Principal principal,
            Model model) {

        // バリデーションチェックがエラーの場合、新規登録画面を表示
        if (res.hasErrors()) {
            return create(dailyReport, principal, model);
        }

        // ログイン中のユーザ情報の取得
        Employee employee = dailyReportService.getLoginUser(principal);

        // ログイン中のユーザ かつ 入力した日付が日報テーブルに既に存在する場合にエラーを表示
        if (dailyReportService.existsReportByEmployeeAndDate(employee, dailyReport.getReportDate())) {
            String errorName = ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR);
            String errorValue = ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR);
            model.addAttribute(errorName, errorValue);
            return create(dailyReport, principal, model);
        }

        dailyReport.setEmployee(employee);
        dailyReportService.save(dailyReport);
        return "redirect:/reports";
    }

    // 日報詳細画面
    @GetMapping("/{id}/")
    public String detail(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("dailyReport", dailyReportService.findById(id));
        return "dailyReport/dailyReportDetail";
    }

    // 日報削除処理
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Principal principal, Model model) {

        // ログイン中のユーザ情報の取得
        Employee employee = dailyReportService.getLoginUser(principal);

        //ログイン中のユーザと削除しようとしている日報の作成者が異なるに場合エラーを表示
        if (!employee.getCode().equals(dailyReportService.findById(id).getEmployee().getCode())) {
            String errorName = ErrorMessage.getErrorName(ErrorKinds.PERMISSION_DENIED_ERROR);
            String errorValue = ErrorMessage.getErrorValue(ErrorKinds.PERMISSION_DENIED_ERROR);
            model.addAttribute(errorName, errorValue);
            return detail(id, model);
        }

        dailyReportService.delete(id);
        return "redirect:/reports";
    }

    // 日報更新画面表示
    @GetMapping("/{id}/update")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute DailyReport dailyReport, Model model) {
        if (id != null) {
            model.addAttribute("dailyReport", dailyReportService.findById(id));
        } else {
            model.addAttribute("dailyReport", dailyReport);
        }
        return "dailyReport/dailyReportUpdate";
    }

    // 日報更新処理
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @ModelAttribute @Validated DailyReport dailyReport,
            BindingResult res, Principal principal, Model model) {

        // バリデーションチェックがエラーの場合、日報更新画面を表示
        if (res.hasErrors()) {
            return edit(null, dailyReport, model);
        }

        // 日報のidを参照し、現在編集中のデータを取得
        DailyReport dailyReportCurrentData = dailyReportService.findById(id);

        // ログイン中のユーザ情報の取得
        Employee employee = dailyReportService.getLoginUser(principal);

        // ログイン中のユーザ かつ 入力された日付が同じデータを取得
        DailyReport existingReport = dailyReportService.findByEmployeeAndDate(employee, dailyReport.getReportDate());

        // 現在編集中の日報と同じ日付が存在している かつ 既存の日報は今編集している日報とidが異なっている場合にエラーを表示
        if (existingReport != null && !Objects.equals(existingReport.getId(), dailyReportCurrentData.getId())) {
            String errorName = ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR);
            String errorValue = ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR);
            model.addAttribute(errorName, errorValue);
            return edit(null, dailyReport, model);
        }

        // 入力された値で日報を更新
        dailyReportService.update(dailyReportCurrentData, dailyReport);

        return "redirect:/reports";
    }
}
