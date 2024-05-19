package db.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class SurchargeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test4")
    @DisplayName("추가요금 정보 조회 성공 테스트")
    void getSurchargeInfoTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/surcharge/info")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(SurchargeController.class))
                .andExpect(handler().methodName("getSurchargeInfo"))
                .andExpect(jsonPath("$.overfee", is(2000)))
        ;
    }
    @Test
    @WithMockUser(username = "test4")
    @DisplayName("추가요금 지불 성공 테스트")
    void getSurchargePayTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/surcharge/pay")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(SurchargeController.class))
                .andExpect(handler().methodName("getSurchargePay"))
        ;
    }
    @Test
    @WithMockUser(username = "test2")
    @DisplayName("추가요금 지불 실패 테스트")
    void getSurchargePayFailureTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/surcharge/pay")
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SurchargeController.class))
                .andExpect(handler().methodName("getSurchargePay"))
                .andExpect(jsonPath("$.message", is("NOT ENOUGH MONEY")))
        ;
    }
}
