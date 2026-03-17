package com.example.sondeptraidemo.repository;

import com.example.sondeptraidemo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a where a.loginName = :loginName")
    Optional<Account> findByLoginName(@Param("loginName") String loginName);
}
