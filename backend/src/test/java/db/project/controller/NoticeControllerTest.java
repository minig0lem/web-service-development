package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.NoticeDto.NoticeDelete;
import db.project.dto.NoticeDto.NoticeCreateAndUpdate;
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
public class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("공지사항 리스트 조회 성공 테스트")
    void getNoticeListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/notice/list/{page}", 1)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("getNoticeList"))
                .andExpect(jsonPath("$.noticeCount", is(2)))
                .andExpect(jsonPath("$.noticeList[0].title", is("test notice title2")))
                .andExpect(jsonPath("$.noticeList[0].date").exists())
                .andExpect(jsonPath("$.noticeList[0].views", is(0)))
                .andExpect(jsonPath("$.noticeList[1].title", is("test notice title")))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("공지사항 상세 정보 조회 성공 테스트")
    void getNoticeInfoTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/notice/info/{noticeId}", 1)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("getNoticeInfo"))
                .andExpect(jsonPath("$.notice_id", is(1)))
                .andExpect(jsonPath("$.user_id", is("admin")))
                .andExpect(jsonPath("$.title", is("test notice title")))
                .andExpect(jsonPath("$.content", is("test notice content")))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.views", is(1)))
                .andExpect(jsonPath("$.author", is(false)))
        ;
    }
    @Test
    @WithMockUser(username = "test")
    @DisplayName("공지사항 상세 정보 조회 실패 테스트 (게시물을 찾을 수 없음)")
    void getNoticeInfoFailureTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/notice/info/{noticeId}", 3)
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("getNoticeInfo"))
                .andExpect(jsonPath("$.message", is("POST NOT FOUND")))
        ;
    }
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @DisplayName("공지사항 작성 성공 테스트")
    void postNoticeCreateTest() throws Exception {
        //Given
        NoticeCreateAndUpdate request = NoticeCreateAndUpdate.builder()
                .title("test title")
                .content("test content")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/notice/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("postNoticeCreate"))
        ;
    }
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @DisplayName("공지사항 수정 성공 테스트")
    void postNoticeUpdateTest() throws Exception {
        //Given
        NoticeCreateAndUpdate request = NoticeCreateAndUpdate.builder()
                .title("update title")
                .content("update content")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/notice/update/{noticeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("postNoticeUpdate"))
        ;
    }
    @Test
    @WithMockUser(username = "admin123", roles = {"USER", "ADMIN"})
    @DisplayName("공지사항 수정 실패 테스트 (공지사항 저자가 아님)")
    void postNoticeUpdateFailureTest() throws Exception {
        //Given
        NoticeCreateAndUpdate request = NoticeCreateAndUpdate.builder()
                .title("update title")
                .content("update content")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/notice/update/{noticeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("postNoticeUpdate"))
                .andExpect(jsonPath("$.message", is("NOT AUTHOR OF THE POST")))
        ;
    }
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @DisplayName("공지사항 삭제 성공 테스트")
    void getNoticeDeleteTest() throws Exception {
        //Given
        NoticeDelete request = NoticeDelete.builder()
                .notice_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/notice/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("getNoticeDelete"))
        ;
    }
    @Test
    @WithMockUser(username = "admin1234", roles = {"USER", "ADMIN"})
    @DisplayName("공지사항 삭제 실패 테스트 (공지사항 저자가 아님)")
    void getNoticeDeleteFailureTest() throws Exception {
        //Given
        NoticeDelete request = NoticeDelete.builder()
                .notice_id(1)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/notice/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(NoticeController.class))
                .andExpect(handler().methodName("getNoticeDelete"))
                .andExpect(jsonPath("$.message", is("NOT AUTHOR OF THE POST")))
        ;
    }
}
