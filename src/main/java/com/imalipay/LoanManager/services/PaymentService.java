package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Payment;
import com.imalipay.LoanManager.dtos.responses.PaymentDetail;

import java.util.List;

public interface PaymentService {
    PaymentDetail makePayment(String email, double payment);
    List<Payment> searchPayment(String email);
}
