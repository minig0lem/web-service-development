package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.BreakdownReportDto.BreakdownReportRepair;
import db.project.dto.BreakdownReportDto.Report;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @Order(1)
    @WithMockUser(username = "test")
    @DisplayName("고장 신고 성공 테스트")
    void postReportTest() throws Exception {
        //Given
        Report request = Report.builder()
                .bike_id("SPB-30063")
                .chain(true)
                .pedal(false)
                .saddle(false)
                .tire(false)
                .terminal(false)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ReportController.class))
                .andExpect(handler().methodName("postReport"))
        ;
    }
    @Test
    @Order(2)
    @WithMockUser(username = "test")
    @DisplayName("고장 신고 실패 테스트 (존재하지 않는 자전거 id)")
    void postReportFailureTest() throws Exception {
        //Given
        Report request = Report.builder()
                .bike_id("SPB-3")
                .chain(true)
                .pedal(false)
                .saddle(false)
                .tire(false)
                .terminal(false)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ReportController.class))
                .andExpect(handler().methodName("postReport"))
                .andExpect(jsonPath("$.message", is("FAILURE TO RECEIVE FAULT REPORT")))
        ;
    }
    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @DisplayName("고장 신고 리스트 조회 성공 테스트")
    void getReportListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/admin/report/list/{page}", 1)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ReportController.class))
                .andExpect(handler().methodName("getReportList"))
                .andExpect(jsonPath("$.reportCount", is(1)))
                .andExpect(jsonPath("$.reportList[0].user_id", is("test")))
                .andExpect(jsonPath("$.reportList[0].bike_id", is("SPB-30063")))
                .andExpect(jsonPath("$.reportList[0].chain", is(true)))
                .andExpect(jsonPath("$.reportList[0].pedal", is(false)))
                .andExpect(jsonPath("$.reportList[0].saddle", is(false)))
                .andExpect(jsonPath("$.reportList[0].tire", is(false)))
                .andExpect(jsonPath("$.reportList[0].terminal", is(false)))
                .andExpect(jsonPath("$.reportList[0].status", is("received")))
        ;
    }
    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @DisplayName("고장 신고 수리 성공 테스트")
    void postReportRepairTest() throws Exception {
        //Given
        BreakdownReportRepair request = BreakdownReportRepair.builder()
                .bike_id("SPB-30063")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/report/repair")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ReportController.class))
                .andExpect(handler().methodName("postReportRepair"))
        ;
    }
}
