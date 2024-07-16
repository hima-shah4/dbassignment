package db.com.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableAsync
@EnableTransactionManagement

@SpringBootApplication
public class TransationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransationserviceApplication.class, args);
	}

}
