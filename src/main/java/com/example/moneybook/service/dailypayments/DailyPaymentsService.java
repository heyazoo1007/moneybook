package com.example.moneybook.service.dailypayments;

import com.example.moneybook.common.exception.ErrorCode;
import com.example.moneybook.common.exception.model.NotFoundException;
import com.example.moneybook.controller.dailypaments.dto.CreateDailyPaymentsRequestDto;
import com.example.moneybook.doamin.dailypayments.DailyPaymentsRepository;
import com.example.moneybook.doamin.member.Member;
import com.example.moneybook.doamin.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyPaymentsService {

    private final DailyPaymentsRepository dailyPaymentsRepository;
    private final MemberRepository memberRepository;

    public void createDailyPayments(CreateDailyPaymentsRequestDto request) {
        Optional<Member> optionalMember = memberRepository.findByEmail(request.getUserEmail());
        if (optionalMember.isEmpty()) {
            throw new NotFoundException("사용자가 존재하지 않습니다.", ErrorCode.NOT_FOUND_USER_EXCEPTION);
        }
    }
}
