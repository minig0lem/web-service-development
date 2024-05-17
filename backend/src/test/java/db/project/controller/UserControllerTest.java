package db.project.controller;

import com.google.gson.Gson;
import db.project.dto.*;
import db.project.repository.RefreshTokenRepository;
import db.project.service.RefreshTokenService;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.Or;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("회원가입 성공 테스트")
    void signupTest() throws Exception {
        //Given
        User user = User.builder()
                .id("gmltjd0326")
                .password("1234")
                .email("gmltjd0326@naver.com")
                .phone_number("010-5287-9601")
                .pw_question(1)
                .pw_answer("중화초")
                .role("user")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("signup"))
                .andExpect(content().string("{}"))
        ;
    }

    @Test
    @Order(2)
    @DisplayName("회원가입 실패 테스트 (중복된 아이디)")
    void signupFailureTest() throws Exception {
        //Given
        User user = User.builder()
                .id("test")
                .password("1234")
                .email("gmltjd0326@naver.com")
                .phone_number("010-5287-9601")
                .pw_question(1)
                .pw_answer("중화초")
                .role("user")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("signup"))
                .andExpect(jsonPath("$.message", is("중복된 아이디입니다.")))
        ;
    }

    @Test
    @Order(3)
    @DisplayName("로그인 성공 테스트")
    void loginTest() throws Exception {
        //Given
        UserLoginRequest request = UserLoginRequest.builder()
                .id("gmltjd0326")
                .password("1234")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.role", is("user")))
        ;
    }

    @Test
    @Order(4)
    @DisplayName("로그인 실패 테스트 (존재하지 않는 아이디 입력)")
    void loginFailureTest1() throws Exception {
        //Given
        UserLoginRequest request = UserLoginRequest.builder()
                .id("gmltjd")
                .password("1234")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.message", containsString("존재하지 않는 아이디")))
        ;
    }

    @Test
    @Order(5)
    @DisplayName("로그인 실패 테스트 (틀린 비밀번호 입력)")
    void loginFailureTest2() throws Exception {
        //Given
        UserLoginRequest request = UserLoginRequest.builder()
                .id("gmltjd0326")
                .password("12345")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.message", containsString("비밀번호가 일치하지 않습니다")))
        ;
    }

    @Test
    @Order(6)
    @WithMockUser
    @DisplayName("로그아웃 성공 테스트")
    void logoutTest() throws Exception {
        //Given
        RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);

        //When
        ResultActions result = mockMvc.perform(
                post("/api/logout")
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("logout"))
        ;
    }

    @Test
    @Order(7)
    @DisplayName("비밀번호 찾기 성공 테스트")
    void findPWTest() throws Exception {
        //Given
        FindPWRequest request = FindPWRequest.builder()
                .id("gmltjd0326")
                .pw_question(1)
                .pw_answer("중화초")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/findPW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("findPW"))
        ;
    }

    @Test
    @Order(8)
    @DisplayName("비밀번호 찾기 실패 테스트 (가입하지 않은 아이디 입력)")
    void findPWFailureTest1() throws Exception {
        //Given
        FindPWRequest request = FindPWRequest.builder()
                .id("gmltjd")
                .pw_question(1)
                .pw_answer("중화초")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/findPW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("findPW"))
                .andExpect(jsonPath("$.message", containsString("가입하지 않은 id입니다")))
        ;
    }

    @Test
    @Order(9)
    @DisplayName("비밀번호 찾기 실패 테스트 (잘못된 비밀번호 찾기 질문 선택)")
    void findPWFailureTest2() throws Exception {
        //Given
        FindPWRequest request = FindPWRequest.builder()
                .id("gmltjd0326")
                .pw_question(2)
                .pw_answer("중화초")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/findPW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("findPW"))
                .andExpect(jsonPath("$.message", containsString("비밀번호 찾기 질문이 틀렸습니다")))
        ;
    }

    @Test
    @Order(10)
    @DisplayName("비밀번호 찾기 실패 테스트 (잘못된 비밀번호 찾기 답변 입력)")
    void findPWFailureTest3() throws Exception {
        //Given
        FindPWRequest request = FindPWRequest.builder()
                .id("gmltjd0326")
                .pw_question(1)
                .pw_answer("면목초")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/findPW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("findPW"))
                .andExpect(jsonPath("$.message", containsString("비밀번호 찾기 답변이 틀렸습니다")))
        ;
    }
    @Test
    @Order(11)
    @DisplayName("비밀번호 변경 성공 테스트")
    void updatePWTest() throws Exception {
        //Given
        UpdatePWRequest request = UpdatePWRequest.builder()
                .id("gmltjd0326")
                .new_password("123")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/findPW/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("updatePW"))
        ;
    }
    @Test
    @Order(12)
    @WithMockUser
    @DisplayName("내 정보 변경 성공 테스트")
    void updateMyInfoTest() throws Exception {
        //Given
        UpdateMyInfoRequest request = UpdateMyInfoRequest.builder()
                .password("1234")
                .email("gmltjd0326@naver.com")
                .phone_number("010-1234-5678")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/myInfo/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("updateMyInfo"))
        ;
    }

    @Test
    @Order(13)
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("회원 정보 리스트 조회 성공 테스트 (page = 1)")
    void userInfoListTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/admin/user/info/list/{page}", 1)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("userInfoList"))
                .andExpect(jsonPath("$.userCount", is(4)))
                .andExpect(jsonPath("$.userInfoList[0].user_id", is("test")))
                .andExpect(jsonPath("$.userInfoList[1].user_id", is("test2")))
        ;
    }
    @Test
    @Order(14)
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("회원 정보 리스트 조회 성공 테스트 (ID 검색) (page = 1)")
    void userInfoListByIdTest() throws Exception {
        //Given
        PostUserInfoListDto request = PostUserInfoListDto.builder()
                .user_id("gml")
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/admin/user/info/list/{page}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("userInfoListById"))
                .andExpect(jsonPath("$.userCount", is(1)))
                .andExpect(jsonPath("$.userInfoList[0].user_id", is("gmltjd0326")))
        ;
    }
}
