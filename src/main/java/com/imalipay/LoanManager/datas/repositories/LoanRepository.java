package com.imalipay.LoanManager.datas.repositories;

import com.imalipay.LoanManager.datas.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}
