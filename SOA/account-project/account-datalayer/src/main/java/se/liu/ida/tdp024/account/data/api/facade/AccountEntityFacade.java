package se.liu.ida.tdp024.account.data.api.facade;
import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountEntityFacade {
    long createAccount(String personKey, String bankKey, String accountType);

    Account find(long id);

    List<Account> findPersonByKey(String personKey);

    boolean debitAccount(long accountId, long amount);

    boolean creditAccount(long accountId, long amount);

    
}
