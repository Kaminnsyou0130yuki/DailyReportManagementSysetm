package com.techacademy.controller;

import com.techacademy.service.DailyReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

//    日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", dailyReportService.findAll().size());
        model.addAttribute("dairyReportList", dailyReportService.findAll());
        return "dailyReport/dailyReportList";

    }

    @GetMapping("/add")
    public String create(Model model) {
        return "dailyReport/dailyReportNew";
    }

}
