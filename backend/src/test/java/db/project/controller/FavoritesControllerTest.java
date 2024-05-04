package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.FavoritesDto.FavoritesChange;
import db.project.dto.FavoritesDto.FavoritesSearch;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoritesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    @DisplayName("대여소 검색 성공 테스트")
    void postFavoriteListTest() throws Exception {
        //Given
        FavoritesSearch request = FavoritesSearch.builder()
                .location("양천구")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/favorites/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FavoritesController.class))
                .andExpect(handler().methodName("postFavoriteList"))
                .andExpect(jsonPath("$.locations.size()", is(3)))
                .andExpect(jsonPath("$.locations[0].location_id", is("ST-1002")))
                .andExpect(jsonPath("$.locations[0].address", containsString("양천구")))
                .andExpect(jsonPath("$.locations[0].favorite", is(true)))
                .andExpect(jsonPath("$.locations[1].location_id", is("ST-1000")))
                .andExpect(jsonPath("$.locations[1].address", containsString("양천구")))
                .andExpect(jsonPath("$.locations[1].favorite", is(false)))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("즐겨찾기 추가 성공 테스트")
    void postFavoritesChangeTest1() throws Exception {
        //Given
        FavoritesChange request = FavoritesChange.builder()
                .location("서울특별시 마포구 양화로 93 427")
                .favorite(true)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/favorites/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FavoritesController.class))
                .andExpect(handler().methodName("postFavoritesChange"))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("즐겨찾기 삭제 성공 테스트")
    void postFavoritesChangeTest2() throws Exception {
        //Given
        FavoritesChange request = FavoritesChange.builder()
                .location("서울특별시 양천구 목동동로 316-6 서울시 도로환경관리센터")
                .favorite(false)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/favorites/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FavoritesController.class))
                .andExpect(handler().methodName("postFavoritesChange"))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("즐겨찾기 추가 실패 테스트 (이미 즐겨찾기한 대여소)")
    void postFavoritesChangeFailureTest() throws Exception {
        //Given
        FavoritesChange request = FavoritesChange.builder()
                .location("서울특별시 양천구 목동동로 316-6 서울시 도로환경관리센터")
                .favorite(true)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/favorites/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(FavoritesController.class))
                .andExpect(handler().methodName("postFavoritesChange"))
                .andExpect(jsonPath("$.message", is("ALREADY ADD FAVORITE")))
        ;
    }
}
