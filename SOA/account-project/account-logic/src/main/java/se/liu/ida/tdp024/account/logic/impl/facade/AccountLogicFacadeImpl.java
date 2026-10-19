package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.util.ServiceEndpoint;
import se.liu.ida.tdp024.account.logic.impl.util.http.HTTPHelper;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private final AccountEntityFacade accountEntityFacade;
    private final HTTPHelper httpHelper;
    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade, HTTPHelper httpHelper) {
        this.accountEntityFacade = accountEntityFacade;
        this.httpHelper = httpHelper;
    }

    @Override
    public boolean create(String personKey, String bankName, String accountType) {
        // Check accountType against allowed values
        if (!accountType.equals("CHECK") && !accountType.equals("SAVINGS")) {
            return false;
        }
        String person = httpHelper.get(ServiceEndpoint.PERSON + "find.key", "key", personKey);
        if (person.isEmpty() || person.equals("null")) {
            return false;
        }
        String bankJson = httpHelper.get(ServiceEndpoint.BANK + "find.name", "name", bankName);
        if (bankJson.isEmpty() || bankJson.equals("null")) {
            return false;
        }
        String bankKey;
        try {
            bankKey = new ObjectMapper().readTree(bankJson).get("key").asText();
        } catch (Exception e) {
            return false;
        }
        return accountEntityFacade.createAccount(personKey, bankKey, accountType) > 0;
    }

    @Override
    public List<Account> findByPerson(String personKey) {
        return accountEntityFacade.findPersonByKey(personKey);
    }


    
}
