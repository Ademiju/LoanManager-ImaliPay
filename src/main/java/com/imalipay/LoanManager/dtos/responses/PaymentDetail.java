package com.imalipay.LoanManager.dtos.responses;

import com.imalipay.LoanManager.datas.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetail {
    private Status loanStatus;
    private Status paymentStatus;
    private String paymentTime;
    private LocalDate dueDate;
    private BigDecimal amountPaid;
    private BigDecimal DebtBalance;

}
