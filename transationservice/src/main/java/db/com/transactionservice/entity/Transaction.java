package db.com.transactionservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long transactionId;
	private long account;
	private double amount;
	private String transactionType;
    @CreatedDate
	private LocalDateTime createdOn;
    
    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }
}
