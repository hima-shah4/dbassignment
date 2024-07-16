package db.com.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import db.com.accountservice.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	
}
