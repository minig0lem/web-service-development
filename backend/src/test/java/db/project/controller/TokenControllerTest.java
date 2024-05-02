package db.project.controller;

import com.google.gson.Gson;
import db.project.config.jwt.TokenProvider;
import db.project.dto.CreateTokenRequest;
import db.project.dto.RefreshToken;
import db.project.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("액세스 토큰 생성 성공 테스트")
    void createAccessTokenTest() throws Exception {
        //Given

        //RefreshToken 생성
        String token = tokenProvider.createToken("test", 30 * 60 * 1000L);
        RefreshToken refreshToken = RefreshToken.builder()
                .id("test")
                .refreshToken(token)
                .build();
        refreshTokenService.save(refreshToken);

        CreateTokenRequest request = CreateTokenRequest.builder()
                .refreshToken(token)
                .build();

        //When
        ResultActions result = mockMvc.perform(
                post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TokenController.class))
                .andExpect(handler().methodName("createAccessToken"))
                .andExpect(jsonPath("$.accessToken").exists())
        ;
    }
}
