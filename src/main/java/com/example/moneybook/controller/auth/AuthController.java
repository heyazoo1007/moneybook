package com.example.moneybook.controller.auth;

import com.example.moneybook.common.config.security.dto.TokenResponseDto;
import com.example.moneybook.common.dto.ApiResponse;
import com.example.moneybook.controller.auth.dto.request.LoginRequestDto;
import com.example.moneybook.controller.auth.dto.request.CompleteAuthEmailRequestDto;
import com.example.moneybook.controller.auth.dto.request.SendAuthEmailRequestDto;
import com.example.moneybook.controller.auth.dto.request.ValidateEmailRequestDto;
import com.example.moneybook.controller.auth.dto.request.CreateMemberRequestDto;
import com.example.moneybook.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/email/validate")
    public ApiResponse<String> validateEmail(
            @Valid @RequestBody ValidateEmailRequestDto request
    ) {
        authService.validateEmail(request.getEmail());
        return ApiResponse.SUCCESS;
    }

    @PostMapping("/email/send")
    public ApiResponse<String> sendAuthEmail(
            @Valid @RequestBody SendAuthEmailRequestDto request
    ) {
        authService.sendAuthEmail(request);
        return ApiResponse.SUCCESS;
    }

    @PostMapping("/email/complete")
    public ApiResponse<String> completeAuthEmail(
            @Valid @RequestBody CompleteAuthEmailRequestDto request
    ) {
        authService.completeAuthEmail(request);
        return ApiResponse.SUCCESS;
    }

    @PostMapping("/signup")
    public ApiResponse<String> createMember(
            @Valid @RequestBody CreateMemberRequestDto request
    ) {
        authService.createMember(request);
        return ApiResponse.SUCCESS;
    }

    @PostMapping("/signin") // endpoint에는 소문자만 사용가능
    public ApiResponse<String> signIn(
            @Valid @RequestBody LoginRequestDto request
    ) {
        String token = authService.signIn(request.getEmail(), request.getPassword());
        return ApiResponse.success(token);
    }
}
