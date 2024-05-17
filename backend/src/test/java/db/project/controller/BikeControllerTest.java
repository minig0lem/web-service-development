package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostBikeCreateDto;
import db.project.dto.PostBikeDeleteDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
@SpringBootTest
@AutoConfigureMockMvc
public class BikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 리스트 조회 성공 테스트(page = 1)")
    void getBikeListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/admin/bike/list/{page}", 1)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("getBikeList"))
                .andExpect(jsonPath("$.bikeCount", is(7)))
                .andExpect(jsonPath("$.bikeList[0].bike_id", is("SPB-30063")))
                .andExpect(jsonPath("$.bikeList[0].address", containsString("서울특별시")))
                .andExpect(jsonPath("$.bikeList[0].status", is("available")))
                .andExpect(jsonPath("$.bikeList[1].bike_id", is("SPB-30074")))
                .andExpect(jsonPath("$.bikeList[1].address", containsString("서울특별시")))
                .andExpect(jsonPath("$.bikeList[1].status", is("available")))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 추가 성공 테스트")
    void postBikeCreateTest() throws Exception {
        //Given
        PostBikeCreateDto request = PostBikeCreateDto.builder()
                .bike_id("SPB-30430")
                .location_id("ST-1002")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/bike/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("postBikeCreate"))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 추가 실패 테스트 (중복된 자전거 ID)")
    void postBikeCreateFailureTest1() throws Exception {
        //Given
        PostBikeCreateDto request = PostBikeCreateDto.builder()
                .bike_id("SPB-30063")
                .location_id("ST-1002")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/bike/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("postBikeCreate"))
                .andExpect(jsonPath("$.message", is("BIKE ID DUPLICATE")))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 추가 실패 테스트 (존재하지 않는 대여소)")
    void postBikeCreateFailureTest2() throws Exception {
        //Given
        PostBikeCreateDto request = PostBikeCreateDto.builder()
                .bike_id("SPB-30975")
                .location_id("ST-2000")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/bike/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("postBikeCreate"))
                .andExpect(jsonPath("$.message", is("NON EXIST LOCATION ID")))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 삭제 성공 테스트")
    void postBikeDeleteTest() throws Exception {
        //Given
        PostBikeDeleteDto request = PostBikeDeleteDto.builder()
                .bike_id("SPB-30400")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/bike/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("postBikeDelete"))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("자전거 삭제 실패 테스트 (존재하지 않는 자전거 ID)")
    void postBikeDeleteFailureTest() throws Exception {
        //Given
        PostBikeDeleteDto request = PostBikeDeleteDto.builder()
                .bike_id("SPB-3")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/bike/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BikeController.class))
                .andExpect(handler().methodName("postBikeDelete"))
                .andExpect(jsonPath("$.message", is("NON EXIST BIKE ID")))
        ;
    }

}
