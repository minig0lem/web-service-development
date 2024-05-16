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

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test")
    @DisplayName("사용자 메인화면 API 성공 테스트")
    void getUserMainTest() throws Exception {
        //Given
        //When
        ResultActions result = mockMvc.perform(
                get("/api/user/main")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MainController.class))
                .andExpect(handler().methodName("getUserMain"))
                .andExpect(jsonPath("$.user_id", is("test")))
                .andExpect(jsonPath("$.email", is("test")))
                .andExpect(jsonPath("$.phone_number", is("test")))
                .andExpect(jsonPath("$.cash", is(0)))
                .andExpect(jsonPath("$.overfee", is(0)))
                .andExpect(jsonPath("$.hour", is(1)))
                .andExpect(jsonPath("$.isRented", is(0)))
                .andExpect(jsonPath("$.bike_id").value(nullValue()))
        ;
    }

}
