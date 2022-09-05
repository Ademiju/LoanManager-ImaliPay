package com.imalipay.LoanManager.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private BigDecimal amount;
    private LocalDate date;
    private int durationInMonth;
    private LocalDate dueDate;
    private final double INTEREST_RATE = 0.08;
    private BigDecimal repayment;
    private String message;
}
