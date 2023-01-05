package com.example.moneybook.doamin.dailypayments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyPaymentsRepository extends JpaRepository<DailyPayments, Long> {
}
