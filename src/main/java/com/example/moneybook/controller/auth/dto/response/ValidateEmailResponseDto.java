package com.example.moneybook.controller.auth.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ValidateEmailResponseDto {

    private String email;
}
