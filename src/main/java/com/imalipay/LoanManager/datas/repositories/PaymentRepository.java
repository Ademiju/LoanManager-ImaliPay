package com.imalipay.LoanManager.datas.repositories;

import com.imalipay.LoanManager.datas.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
