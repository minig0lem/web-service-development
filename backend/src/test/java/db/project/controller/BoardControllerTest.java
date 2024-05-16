package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.BoardDto.BoardDelete;
import db.project.dto.BoardDto.BoardCreateAndUpdate;
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
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 조회 실패 테스트 (게시물을 찾을 수 없음)")
    void getBoardInfoFailureTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/board/info/{boardId}",5)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("getBoardInfo"))
                .andExpect(jsonPath("$.message", is("POST NOT FOUND")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 생성 성공 테스트")
    void postBoardCreateTest() throws Exception {
        //Given
        BoardCreateAndUpdate request = BoardCreateAndUpdate.builder()
                .title("title")
                .content("content")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardCreate"))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 수정 성공 테스트")
    void postBoardUpdateTest() throws Exception {
        //Given
        BoardCreateAndUpdate request = BoardCreateAndUpdate.builder()
                .title("title")
                .content("content update")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/update/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardUpdate"))
        ;
    }
    @Test
    @WithMockUser
    @DisplayName("게시물 수정 실패 테스트 (게시물 저자가 아님)")
    void postBoardUpdateFailureTest1() throws Exception {
        //Given
        BoardCreateAndUpdate request = BoardCreateAndUpdate.builder()
                .title("title")
                .content("content update")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/update/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardUpdate"))
                .andExpect(jsonPath("$.message", is("NOT AUTHOR OF THE POST")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 수정 실패 테스트 (게시물을 찾을 수 없음)")
    void postBoardUpdateFailureTest2() throws Exception {
        //Given
        BoardCreateAndUpdate request = BoardCreateAndUpdate.builder()
                .title("title")
                .content("content update")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/update/{boardId}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardUpdate"))
                .andExpect(jsonPath("$.message", is("POST NOT FOUND")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 삭제 성공 테스트")
    void postBoardDeleteTest() throws Exception {
        //Given
        BoardDelete request = BoardDelete.builder()
                .board_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardDelete"))
        ;
    }
    @Test
    @WithMockUser
    @DisplayName("게시물 삭제 실패 테스트 (게시물 저자가 아님)")
    void postBoardDeleteFailureTest1() throws Exception {
        //Given
        BoardDelete request = BoardDelete.builder()
                .board_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardDelete"))
                .andExpect(jsonPath("$.message", is("NOT AUTHOR OF THE POST")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시물 삭제 실패 테스트 (게시물을 찾을 수 없음)")
    void postBoardDeleteFailureTest2() throws Exception {
        //Given
        BoardDelete request = BoardDelete.builder()
                .board_id(5)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardController.class))
                .andExpect(handler().methodName("postBoardDelete"))
                .andExpect(jsonPath("$.message", is("POST NOT FOUND")))
        ;
    }
}
