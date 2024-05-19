package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.PostTicketGiftDto;
import db.project.dto.PostTicketPurchaseDto;
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
public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("이용권 리스트 조회 성공 테스트")
    void getTicketListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/ticket")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("getTicketList"))
                .andExpect(jsonPath("$.tickets.size()", is(3)))
                .andExpect(jsonPath("$.tickets[0].hour", is(1)))
                .andExpect(jsonPath("$.tickets[0].price", is(1000)))
                .andExpect(jsonPath("$.tickets[1].hour", is(2)))
                .andExpect(jsonPath("$.tickets[1].price", is(2000)))
                .andExpect(jsonPath("$.tickets[2].hour", is(24)))
                .andExpect(jsonPath("$.tickets[2].price", is(24000)))
        ;
    }
    @Test
    @WithMockUser(username = "test4")
    @DisplayName("이용권 구매 성공 테스트")
    void postPurchaseTest() throws Exception {
        //Given
        PostTicketPurchaseDto request = PostTicketPurchaseDto.builder()
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postPurchase"))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("이용권 구매 실패 테스트 (이용권 중복)")
    void postPurchaseFailureTest() throws Exception {
        //Given
        PostTicketPurchaseDto request = PostTicketPurchaseDto.builder()
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postPurchase"))
                .andExpect(jsonPath("$.message", is("ALREADY HAVE TICKET")))
        ;
    }
    @Test
    @WithMockUser(username = "test2")
    @DisplayName("이용권 구매 실패 테스트 (소지금 부족)")
    void postPurchaseFailureTest2() throws Exception {
        //Given
        PostTicketPurchaseDto request = PostTicketPurchaseDto.builder()
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postPurchase"))
                .andExpect(jsonPath("$.message", is("NOT ENOUGH MONEY")))
        ;
    }
    @Test
    @WithMockUser(username = "test4")
    @DisplayName("이용권 선물 성공 테스트")
    void postGiftTest() throws Exception {
        //Given
        PostTicketGiftDto request = PostTicketGiftDto.builder()
                .user_id("test2")
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket/gift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postGift"))
        ;
    }
    @Test
    @WithMockUser(username = "test4")
    @DisplayName("이용권 선물 실패 테스트 (받는 사람 이용권 중복)")
    void postGiftFailureTest() throws Exception {
        //Given
        PostTicketGiftDto request = PostTicketGiftDto.builder()
                .user_id("test")
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket/gift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postGift"))
                .andExpect(jsonPath("$.message", is("ALREADY HAVE TICKET")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("이용권 선물 실패 테스트 (소지금 부족)")
    void postGiftFailureTest2() throws Exception {
        //Given
        PostTicketGiftDto request = PostTicketGiftDto.builder()
                .user_id("test2")
                .hour(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/ticket/gift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(TicketController.class))
                .andExpect(handler().methodName("postGift"))
                .andExpect(jsonPath("$.message", is("NOT ENOUGH MONEY")))
        ;
    }
}
