package db.com.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_master")
public class User {
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private long id;
	 private String name;
	 private String username;
	 private String password;
	 private String email;
	 private String phonenumber;
//	 @CreationTimestamp
//	 private LocalDateTime creationDateTime;
//	 @UpdateTimestamp
//	 private LocalDateTime modificationDateTime;
}
