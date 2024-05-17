package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostRentalHistoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class RentalHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    @DisplayName("대여/반납 이력 조회 성공 테스트")
    void postRentalHistoryTest() throws Exception {
        //Given
        PostRentalHistoryDto request = PostRentalHistoryDto.builder()
                .start_date(String.valueOf(LocalDateTime.now()).substring(0, 10))
                .end_date(String.valueOf(LocalDateTime.now().plusDays(3)).substring(0, 10))
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/rentalHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalHistoryController.class))
                .andExpect(handler().methodName("postRentalHistory"))
                .andExpect(jsonPath("$.rentalInfo.size()", is(2)))
                .andExpect(jsonPath("$.rentalInfo[0].bike_id", is("SPB-30063")))
                .andExpect(jsonPath("$.rentalInfo[0].start_location", is("ST-10")))
                .andExpect(jsonPath("$.rentalInfo[0].end_location", is("ST-10")))
                .andExpect(jsonPath("$.rentalInfo[1].bike_id", is("SPB-30074")))
        ;
    }
}
