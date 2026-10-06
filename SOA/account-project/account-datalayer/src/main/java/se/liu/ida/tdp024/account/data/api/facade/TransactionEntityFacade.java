package se.liu.ida.tdp024.account.data.api.facade;
import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

public interface TransactionEntityFacade {
    long createTransaction(String transactionType, long accountId, long amount, String status);
    List<Transaction> findTransactionsByAccountId(long accountId);
}
 
    