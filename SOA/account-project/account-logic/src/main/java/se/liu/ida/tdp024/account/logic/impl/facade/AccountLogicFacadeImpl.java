package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Service;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.util.ServiceEndpoint;



@Service
public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private final AccountEntityFacade accountEntityFacade;
    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public boolean create(String personKey, String bankName, String accountType) {
        // HTTP calls to port 8060 and 8070 will go here
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Account> findByPerson(String personKey) {
  




        return accountEntityFacade.findPersonByKey(personKey);
    }
}

