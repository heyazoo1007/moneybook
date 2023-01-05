package com.example.moneybook.controller.dailypaments;

import com.example.moneybook.common.dto.ApiResponse;
import com.example.moneybook.controller.dailypaments.dto.CreateDailyPaymentsRequestDto;
import com.example.moneybook.service.dailypayments.DailyPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/v1/dailypayments")
@RestController
@RequiredArgsConstructor
public class DailyPaymentsController {

    private final DailyPaymentsService dailyPaymentsService;

    @PostMapping("/create")
    public ApiResponse<String> createDailyPayments(
            @Valid @RequestBody CreateDailyPaymentsRequestDto request) {
        dailyPaymentsService.createDailyPayments(request);
        return ApiResponse.SUCCESS;
    }
}
