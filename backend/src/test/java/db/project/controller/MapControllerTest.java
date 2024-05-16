package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostMapLocationInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
@SpringBootTest
@AutoConfigureMockMvc
public class MapControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("대여소 리스트 조회 성공 테스트 (사용자)")
    void getLocationListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/map")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MapController.class))
                .andExpect(handler().methodName("getLocationList"))
                .andExpect(jsonPath("$.locations.size()", is(5)))
                .andExpect(jsonPath("$.locations[0].bikeCount", is(5)))
                .andExpect(jsonPath("$.locations[0].address", containsString("서울특별시")))
                .andExpect(jsonPath("$.locations[1].bikeCount", is(2)))
        ;
    }

    @Test
    @WithMockUser(username = "test")
    @DisplayName("대여소 상세 정보 조회 성공 테스트")
    void postLocationInfoTest() throws Exception {
        //Given
        PostMapLocationInfoDto request = PostMapLocationInfoDto.builder()
                .latitude("37.5299")
                .longitude("126.876541")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/map/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MapController.class))
                .andExpect(handler().methodName("postLocationInfo"))
                .andExpect(jsonPath("$.location_id", is("ST-1002")))
                .andExpect(jsonPath("$.address", is("서울특별시 양천구 목동동로 316-6 서울시 도로환경관리센터")))
                .andExpect(jsonPath("$.location_status", is("available")))
                .andExpect(jsonPath("$.favorite", is(true)))
                .andExpect(jsonPath("$.bike.size()", is(0)))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("대여소 상세 정보 조회 실패 테스트(잘못된 경도, 위도 값 입력)")
    void postLocationInfoFailureTest() throws Exception {
        //Given
        PostMapLocationInfoDto request = PostMapLocationInfoDto.builder()
                .latitude("37.524")
                .longitude("126.876541")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/map/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MapController.class))
                .andExpect(handler().methodName("postLocationInfo"))
                .andExpect(jsonPath("$.message", is("FAILURE VIEW INFO")))
        ;
    }
}
