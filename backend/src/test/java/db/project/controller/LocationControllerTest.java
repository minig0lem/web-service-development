package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostLocationCreateDto;
import db.project.dto.PostLocationDeleteDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("대여소 리스트 조회 성공 테스트 (관리자)")
    void getLocationListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/admin/location/list/{page}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(LocationController.class))
                .andExpect(handler().methodName("getLocationList"))
                .andExpect(jsonPath("$.locationCount", is(5)))
                .andExpect(jsonPath("$.locationList[0].location_id", is("ST-10")))
                .andExpect(jsonPath("$.locationList[0].address", is("서울특별시 마포구 양화로 93 427")))
                .andExpect(jsonPath("$.locationList[0].bikeCount", is(5)))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("대여소 추가 성공 테스트")
    void postLocationCreateTest() throws Exception {
        //Given
        PostLocationCreateDto request = PostLocationCreateDto.builder()
                .location_id("ST-1005")
                .address("서울특별시 양천구 신정동 310-8 신트리공원 입구")
                .latitude("37.51395")
                .longitude("126.856056")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/location/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(LocationController.class))
                .andExpect(handler().methodName("postLocationCreate"))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("대여소 추가 실패 테스트 (이미 존재하는 대여소 ID)")
    void postLocationCreateFailureTest() throws Exception {
        //Given
        PostLocationCreateDto request = PostLocationCreateDto.builder()
                .location_id("ST-10")
                .address("서울특별시 양천구 신정동 310-8 신트리공원 입구")
                .latitude("37.51395")
                .longitude("126.856056")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/location/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(LocationController.class))
                .andExpect(handler().methodName("postLocationCreate"))
                .andExpect(jsonPath("$.message", is("LOCATION ID DUPLICATE")))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("대여소 삭제 성공 테스트")
    void postLocationDeleteTest() throws Exception {
        //Given
        PostLocationDeleteDto request = PostLocationDeleteDto.builder()
                .location_id("ST-10")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/location/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(LocationController.class))
                .andExpect(handler().methodName("postLocationDelete"))
        ;
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("대여소 삭제 실패 테스트 (존재하지 않는 대여소 ID)")
    void postLocationDeleteFailureTest() throws Exception {
        //Given
        PostLocationDeleteDto request = PostLocationDeleteDto.builder()
                .location_id("ST-1234")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/location/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(LocationController.class))
                .andExpect(handler().methodName("postLocationDelete"))
                .andExpect(jsonPath("$.message", is("NON EXIST LOCATION ID")))
        ;
    }

}
