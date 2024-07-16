package db.com.transactionservice.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import db.com.transactionservice.service.TransactionService;
import db.com.transactionservice.vo.request.TransactionRequest;
import db.com.transactionservice.vo.response.TransactionHistoryResponse;
import db.com.transactionservice.vo.response.TransactionResponse;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping
	public ResponseEntity<TransactionResponse> transact(@RequestBody TransactionRequest transactionServiceRequest) {
		return ResponseEntity.ok(transactionService.transact(transactionServiceRequest));
	}
	
	@GetMapping("/history")
	public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(@RequestParam long account, 
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
		System.out.println("fromDate ="+ fromDate);
		return ResponseEntity.ok(transactionService.getTransactionHistory(account, fromDate, toDate));
	}
	
}
