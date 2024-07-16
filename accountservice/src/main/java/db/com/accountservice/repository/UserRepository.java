package db.com.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import db.com.accountservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}
