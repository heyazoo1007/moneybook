package com.example.moneybook.doamin.dailypayments;

import com.example.moneybook.doamin.Category;
import com.example.moneybook.doamin.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DailyPayments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    private Long paidAmount;

    private String paidWhere;

    private String methodOfPayment;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String hashTag;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
