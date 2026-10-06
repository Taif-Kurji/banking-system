package se.liu.ida.tdp024.account.logic.api.facade;

import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountLogicFacade {
    boolean create(String personKey, String bankName, String accountType); 
    List<Account> findByPerson(String personKey);

}

