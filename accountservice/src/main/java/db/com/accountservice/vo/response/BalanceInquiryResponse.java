package db.com.accountservice.vo.response;

import lombok.Data;

@Data
public class BalanceInquiryResponse {
   private long accountNumber;
   private double amount;
}
