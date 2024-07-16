package db.com.transactionservice.vo.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionHistoryResponse {
   private List<TransactionRecord> transactionlist;
   private String statusMsg;
}
