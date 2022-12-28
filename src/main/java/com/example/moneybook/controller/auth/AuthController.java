package com.example.moneybook.controller.auth;

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
    public void validateEmail(@Valid @RequestBody ValidateEmailRequestDto request) {
        authService.validateEmail(request.getEmail());
    }

    @PostMapping("/email/send")
    public void sendAuthEmail(@Valid @RequestBody SendAuthEmailRequestDto request) {
        authService.sendAuthEmail(request);
    }

    @PostMapping("/email/complete")
    public void completeAuthEmail(@Valid @RequestBody CompleteAuthEmailRequestDto request) {
        authService.completeAuthEmail(request);
    }

    @PostMapping("/signup")
    public void createMember(@Valid @RequestBody CreateMemberRequestDto request) {
        authService.createMember(request);
    }
}
