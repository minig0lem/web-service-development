package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostRentalRentDto;
import db.project.dto.PostRentalReturnDto;
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
public class RentalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @WithMockUser(username = "test")
    @DisplayName("자전거 대여 성공 테스트")
    void postRentTest() throws Exception {
        //Given
        PostRentalRentDto request = PostRentalRentDto.builder()
                .bike_id("SPB-30063")
                .start_location("ST-10")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("postRent"))
        ;
    }
    @Test
    @Order(2)
    @WithMockUser(username = "test3")
    @DisplayName("자전거 대여 실패 테스트 (이용권 미보유)")
    void postRentFailureTest1() throws Exception {
        //Given
        PostRentalRentDto request = PostRentalRentDto.builder()
                .bike_id("SPB-30063")
                .start_location("ST-10")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("postRent"))
                .andExpect(jsonPath("$.message", is("DON'T HAVE TICKET")))
        ;
    }
    @Test
    @Order(3)
    @WithMockUser(username = "test2")
    @DisplayName("자전거 대여 실패 테스트 (미납금 존재)")
    void postRentFailureTest2() throws Exception {
        //Given
        PostRentalRentDto request = PostRentalRentDto.builder()
                .bike_id("SPB-30063")
                .start_location("ST-10")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("postRent"))
                .andExpect(jsonPath("$.message", is("EXIST UNPAID AMOUNT")))
        ;
    }
    @Test
    @Order(4)
    @WithMockUser(username = "test")
    @DisplayName("자전거 반납 성공 테스트")
    void postReturnTest() throws Exception {
        //Given
        PostRentalReturnDto request = PostRentalReturnDto.builder()
                .bike_id("SPB-30063")
                .end_location("ST-100")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("postReturn"))
                .andExpect(jsonPath("$.overfee", is(0)))
                .andExpect(jsonPath("$.fee").exists())
                .andExpect(jsonPath("$.withdraw", is(0)))
        ;
    }
}
