package se.liu.ida.tdp024.account.logic.impl.facade;
import java.util.List;
import org.springframework.stereotype.Service;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.logic.impl.util.kafka.EventPublisher;


@Service
public class TransactionLogicFacadeImpl implements TransactionLogicFacade {
    
    private final TransactionEntityFacade transactionEntityFacade;
    private final  AccountEntityFacade accountEntityFacade;
    private final EventPublisher eventPublisher;
    
    public TransactionLogicFacadeImpl(TransactionEntityFacade transactionEntityFacade, AccountEntityFacade accountEntityFacade, EventPublisher eventPublisher) {
        this.transactionEntityFacade = transactionEntityFacade;
        this.accountEntityFacade = accountEntityFacade;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean debit(long id, long amount) {
        boolean ok = accountEntityFacade.debitAccount(id, amount);
        publish("DEBIT", id, amount, ok);
        return ok;

    }

    @Override
    public boolean credit(long id, long amount) {
        boolean ok = accountEntityFacade.creditAccount(id, amount);
        publish("CREDIT", id, amount, ok);
        return ok;
    }

    @Override
    public List<Transaction> findByAccount(long id) {
        return transactionEntityFacade.findTransactionsByAccountId(id);
    }

    private void publish(String type, long accountId, long amount, boolean ok) {
        eventPublisher.publish("transactions", type + " account=" + accountId + " amount=" + amount + " status=" + (ok ? "OK" : "FAILED"));


    }

}