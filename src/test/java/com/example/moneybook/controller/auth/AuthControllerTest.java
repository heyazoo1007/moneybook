package com.example.moneybook.controller.auth;

import antlr.Token;
import com.example.moneybook.common.config.security.JwtTokenProvider;
import com.example.moneybook.common.config.security.dto.TokenResponseDto;
import com.example.moneybook.common.exception.ErrorCode;
import com.example.moneybook.common.exception.model.ConflictException;
import com.example.moneybook.common.exception.model.MoneyBookException;
import com.example.moneybook.controller.auth.dto.request.CreateMemberRequestDto;
import com.example.moneybook.controller.auth.dto.request.LoginRequestDto;
import com.example.moneybook.controller.auth.dto.request.ValidateEmailRequestDto;
import com.example.moneybook.controller.auth.dto.response.CreateMemberResponseDto;
import com.example.moneybook.controller.auth.dto.response.ValidateEmailResponseDto;
import com.example.moneybook.doamin.member.Member;
import com.example.moneybook.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static com.example.moneybook.common.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("??????_validateEmail")
    void ??????_validateEmail() throws Exception {
        // given
        ValidateEmailRequestDto request = ValidateEmailRequestDto.builder()
                .email("abc@naver.com")
                .build();

        given(authService.validateEmail(request.getEmail()))
                .willReturn(ValidateEmailResponseDto.builder()
                        .email(request.getEmail())
                        .build());

        // when

        // then
        mockMvc.perform(post("/v1/auth/email/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("??????_validateEmail_?????????_??????")
    void ??????_validateEmail_?????????_??????() throws Exception {
        // given
        given(authService.validateEmail(anyString()))
                .willThrow(new ConflictException(
                                "?????? ???????????? ??????????????????.",
                                CONFLICT_USER_EXCEPTION)
                );

        // when

        // then
        mockMvc.perform(post("/v1/auth/email/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ValidateEmailRequestDto("abc@naver.com"
                                )
                        )))
                .andDo(print())
                .andExpect(jsonPath("$.status")
                        .value(CONFLICT_USER_EXCEPTION.getStatus()))
                .andExpect(jsonPath("$.message")
                        .value(CONFLICT_USER_EXCEPTION.getMessage()));
    }

    @Test
    @WithMockUser
    @DisplayName("??????_????????????") // ???????????? ????????? ?????? ?????? ?????? ?????? ??????
    void ??????_createMember() throws Exception {
        // given
        CreateMemberRequestDto request = CreateMemberRequestDto.builder()
                .memberName("hello")
                .email("abc@naver.com")
                .password("password")
                .build();
        given(authService.createMember(request))
                .willReturn(CreateMemberResponseDto.builder()
                        .memberId(1L)
                        .memberName("hello")
                        .email("abc@naver.com")
                        .password("password")
                        .createdAt(LocalDateTime.now())
                        .build()
                );

        // when

        // then
        mockMvc.perform(post("/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)
                ))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // ???????????? ?????? ???????????? ????????? ?????? ??? ????????? ???????????????.

    @Test
    @WithMockUser
    @DisplayName("??????_?????????")
    void ??????_signIn() throws Exception {
        // given
        String accessToken = "accessToken";

        LoginRequestDto request = LoginRequestDto.builder()
                .email("abc@naver.com")
                .password("password")
                .build();

        // when
        given(authService.signIn(request.getEmail(), request.getPassword()))
                .willReturn(accessToken);

        // then
        mockMvc.perform(post("/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data")
                        .value(accessToken))
                .andDo(print());
    }

    // ????????? ?????? ???????????? ????????? ?????? ??? ????????? ???????????????.
}