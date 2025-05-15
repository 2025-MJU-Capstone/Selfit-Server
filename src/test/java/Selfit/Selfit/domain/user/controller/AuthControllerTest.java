package Selfit.Selfit.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import selfit.selfit.domain.user.dto.UserLoginRequestDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    // 로그인 테스트
    @Autowired private MockMvc mockMvc;

    @Autowired private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .name("테스트유저")
                .age(30)
                .email("test@example.com")
                .accountId("testuser")
                .password(passwordEncoder.encode("password"))
                .nickname("테스터")
                .gender("M")
                .build());
    }

    @Test
    void loginSuccess() throws Exception{
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setAccountId("testuser");
        loginDto.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 인증 성공"))
                .andExpect(jsonPath("$.data.accountId").value("testuser"))
                .andExpect(jsonPath("$.data.nickname").value("테스터"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.jwt").isNotEmpty());
    }

    @Test
    void loginWithInvalidPassword_returnsUnauthorized() throws Exception {
        // given
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setAccountId("testuser");
        loginDto.setPassword("wrongpw");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}