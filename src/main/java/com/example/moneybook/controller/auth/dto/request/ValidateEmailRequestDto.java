package com.example.moneybook.controller.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class ValidateEmailRequestDto {
    @Email
    private String email;
}
