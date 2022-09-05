package com.imalipay.LoanManager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoanRequest {
    private String email;
    private int amount;
    private int durationInMonth;
}
