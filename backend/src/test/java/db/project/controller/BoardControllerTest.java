package db.project.controller;

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
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("게시판 리스트 조회 성공 테스트")
    void getBoardListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/board/list/{page}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("getBoardList"))
                .andExpect(jsonPath("$.boardCount", is(1)))
                .andExpect(jsonPath("$.boardList[0].title", is("test title")))
                .andExpect(jsonPath("$.boardList[0].views", is(0)))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 조회 성공 테스트")
    void getBoardInfoTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/board/info/{boardId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("getBoardInfo"))
                .andExpect(jsonPath("$.user_id", is("test")))
                .andExpect(jsonPath("$.title", is("test title")))
                .andExpect(jsonPath("$.content", is("test content")))
                .andExpect(jsonPath("$.comments[0].content", is("test comment")))
        ;
    }
}
