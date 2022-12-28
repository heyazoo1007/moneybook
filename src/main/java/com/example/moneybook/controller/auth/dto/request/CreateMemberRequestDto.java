package com.example.moneybook.controller.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateMemberRequestDto {
    @NotNull
    private String memberName;

    @NotNull
    private String password;

    @Email
    private String email;
}
