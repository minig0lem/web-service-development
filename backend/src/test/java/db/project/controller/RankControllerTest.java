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
public class RankControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    @DisplayName("이용시간 랭킹 조회 성공 테스트")
    void getRankTimeTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/rank/time")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RankController.class))
                .andExpect(handler().methodName("getRankTime"))
                .andExpect(jsonPath("$.user_ranking", is(1)))
                .andExpect(jsonPath("$.user_id", is("test")))
                .andExpect(jsonPath("$.user_duraiton_time", is("100")))
                .andExpect(jsonPath("$.rank.size()", is(3)))
        ;
    }

    @Test
    @WithMockUser(username = "test")
    @DisplayName("이용횟수 랭킹 조회 성공 테스트")
    void getRankCountTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/rank/count")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RankController.class))
                .andExpect(handler().methodName("getRankCount"))
                .andExpect(jsonPath("$.user_ranking", is(1)))
                .andExpect(jsonPath("$.user_id", is("test")))
                .andExpect(jsonPath("$.user_using_count", is(2)))
                .andExpect(jsonPath("$.rank.size()", is(3)))
        ;
    }
}
