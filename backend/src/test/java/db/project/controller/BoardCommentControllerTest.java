package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.BoardCommentDto.BoardCommentDelete;
import db.project.dto.BoardCommentDto.BoardCommentCreateAndUpdate;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BoardCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시글 댓글 작성 성공 테스트")
    void postCreateCommentTest() throws Exception {
        //Given
        BoardCommentCreateAndUpdate request = BoardCommentCreateAndUpdate.builder()
                .content("test comment")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/comment/create/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardCommentController.class))
                .andExpect(handler().methodName("postCreateComment"))
        ;
    }

    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시글 댓글 삭제 성공 테스트")
    void postDeleteCommentTest() throws Exception {
        //Given
        BoardCommentDelete request = BoardCommentDelete.builder()
                .board_id(1)
                .comment_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BoardCommentController.class))
                .andExpect(handler().methodName("postDeleteComment"))
        ;
    }
    @Test
    @WithMockUser
    @DisplayName("게시글 댓글 삭제 실패 테스트 (댓글 저자가 아님)")
    void postDeleteCommentFailureTest1() throws Exception {
        //Given
        BoardCommentDelete request = BoardCommentDelete.builder()
                .board_id(1)
                .comment_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardCommentController.class))
                .andExpect(handler().methodName("postDeleteComment"))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("게시글 댓글 삭제 실패 테스트 (댓글을 찾을 수 없음)")
    void postDeleteCommentFailureTest2() throws Exception {
        //Given
        BoardCommentDelete request = BoardCommentDelete.builder()
                .board_id(1)
                .comment_id(3)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/board/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(BoardCommentController.class))
                .andExpect(handler().methodName("postDeleteComment"))
        ;
    }
}
