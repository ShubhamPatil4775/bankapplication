package com.usk.bank.repository;

import com.usk.bank.entity.BankTransaction;
import com.usk.bank.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
    List<BankTransaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    List<BankTransaction> findByAccountNumberAndTransactionDateBetweenOrderByTransactionDateDesc(String accountNumber, Date startDate, Date endDate);
    List<BankTransaction> findByAccountNumberAndTypeOrderByTransactionDateDesc(String accountNumber, TransactionType type);
    boolean existsByTransactionId(String Transaction);
}
