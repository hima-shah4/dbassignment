package db.com.transactionservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import db.com.transactionservice.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findByAccountAndCreatedOnBetween(long account, LocalDateTime fromDate, LocalDateTime toDate);
}