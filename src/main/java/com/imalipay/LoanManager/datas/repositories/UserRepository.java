package com.imalipay.LoanManager.datas.repositories;

import com.imalipay.LoanManager.datas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Integer> {

}
