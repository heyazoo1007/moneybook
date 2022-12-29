package com.example.moneybook.controller.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class MemberLoginRequestDto {

    @Email
    private String email;
    private String password;
}
