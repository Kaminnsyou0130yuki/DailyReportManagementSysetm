package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.techacademy.entity.DailyReport;

import lombok.With;

@SpringBootTest
public class DailyReportControllerTest {

    private MockMvc mockMvc;

    private final WebApplicationContext webApplicationContext;

    public DailyReportControllerTest(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    // 日報一覧画面
    @Test
    @WithMockUser(username = "1")
    void testList() throws Exception {
        MvcResult result = mockMvc.perform(get("/reports")).andExpect(status().isOk())
                .andExpect(model().attributeExists("dailyReportList")).andExpect(model().hasNoErrors())
                .andExpect(view().name("dailyReport/dailyReportList")).andReturn();

        @SuppressWarnings("unchecked")
        List<DailyReport> dailyReportList = (List<DailyReport>) result.getModelAndView().getModel()
                .get("dailyReportList");

        // 日報番号1のテスト
        DailyReport dailyReport1 = dailyReportList.stream().filter(e -> e.getId() == 1).findFirst().get();
        assertEquals(dailyReport1.getId(), 1);
        assertEquals(dailyReport1.getTitle(), "煌木　太郎の記載、タイトル");
        assertEquals(dailyReport1.getContent(), "煌木　太郎の記載、内容");

        // 日報番号2のテスト
        DailyReport dailyReport2 = dailyReportList.stream().filter(e -> e.getId() == 2).findFirst().get();
        assertEquals(dailyReport2.getId(), 2);
        assertEquals(dailyReport2.getTitle(), "田中　太郎の記載、タイトル");
        assertEquals(dailyReport2.getContent(), "田中　太郎の記載、内容");
    }

    // 日報詳細画面
    @Test
    @WithMockUser(username = "1")
    void testDetail1() throws Exception {
        MvcResult result = mockMvc.perform(get("/reports/1/")).andExpect(status().isOk())
                .andExpect(model().attributeExists("dailyReport")).andExpect(model().hasNoErrors())
                .andExpect(view().name("dailyReport/dailyReportDetail")).andReturn();

        DailyReport dailyReport = (DailyReport) result.getModelAndView().getModel().get("dailyReport");
        assertEquals(dailyReport.getId(), 1);
        assertEquals(dailyReport.getTitle(), "煌木　太郎の記載、タイトル");
        assertEquals(dailyReport.getContent(), "煌木　太郎の記載、内容");
    }

    @Test
    @WithMockUser(username = "2")
    void testDetail2() throws Exception {
        MvcResult result = mockMvc.perform(get("/reports/2/")).andExpect(status().isOk())
                .andExpect(model().attributeExists("dailyReport")).andExpect(model().hasNoErrors())
                .andExpect(view().name("dailyReport/dailyReportDetail")).andReturn();

        DailyReport dailyReport = (DailyReport) result.getModelAndView().getModel().get("dailyReport");
        assertEquals(dailyReport.getId(), 2);
        assertEquals(dailyReport.getTitle(), "田中　太郎の記載、タイトル");
        assertEquals(dailyReport.getContent(), "田中　太郎の記載、内容");
    }

}
