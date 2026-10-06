package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private AccountEntityFacade accountEntityFacade;    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    //Tillfällig implementation av createAccount, find och findAll metoderna
    @Override
    public boolean create(String personKey, String bankName, String accountType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Account> findByPerson(String personKey) {
        return accountEntityFacade.findPersonByKey(personKey);
    }
}

