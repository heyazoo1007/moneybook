package com.example.moneybook.controller.auth.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequestDto {
    @NotNull
    private String memberName;

    @Email
    private String email;

    @NotNull
    private String password;
}
