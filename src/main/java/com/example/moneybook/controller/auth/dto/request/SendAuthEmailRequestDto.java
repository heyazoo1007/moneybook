package com.example.moneybook.controller.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class SendAuthEmailRequestDto {

    @Email
    private String email;
}
