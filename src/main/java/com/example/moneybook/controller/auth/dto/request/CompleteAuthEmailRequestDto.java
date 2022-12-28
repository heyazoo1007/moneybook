package com.example.moneybook.controller.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class CompleteAuthEmailRequestDto {
    @Email
    private String email;
    @NotNull
    private String authKey;
}
