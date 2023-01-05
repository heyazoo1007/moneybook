package com.example.moneybook.controller.dailypaments.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDailyPaymentsRequestDto {

    private String userEmail; // 지출 내역 저장할 때마다 사용자 이메일 포함해서 전송?

    @Min(0)
    private Long paidAmount;

    @Max(50)
    private String paidWhere;

    private String methodOfPayment;

    private String categoryName;

    @Max(20)
    private String hashTag;
}
