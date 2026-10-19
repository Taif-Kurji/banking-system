package se.liu.ida.tdp024.account.logic.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.util.ServiceEndpoint;
import se.liu.ida.tdp024.account.logic.test.util.FakeHTTPHelper;

public class AccountLogicFacadeTest {


    //--- Unit under test ---//
    public AccountLogicFacade accountLogicFacade;
    public StorageFacade storageFacade;

    @Before
    public void setUp() {
        FakeHTTPHelper httpHelper = new FakeHTTPHelper();
        httpHelper.responses.put(ServiceEndpoint.PERSON + "find.key?key=p1", "{\"key\":\"p1\",\"name\":\"Xena\"}");
        httpHelper.responses.put(ServiceEndpoint.PERSON + "find.key?key=p2", "{\"key\":\"p2\",\"name\":\"Zorro\"}");
        httpHelper.responses.put(ServiceEndpoint.BANK + "find.name?name=SWEDBANK", "{\"name\":\"SWEDBANK\",\"key\":\"1\"}");
        accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB(), httpHelper);
        storageFacade = new StorageFacadeDB();
    }

    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }



    @Test
    public void testCreate() {
        Assert.assertTrue(accountLogicFacade.create("p1", "SWEDBANK", "CHECK"));
        Assert.assertTrue(accountLogicFacade.create("p1", "SWEDBANK", "SAVINGS"));

        List<Account> accounts = accountLogicFacade.findByPerson("p1");
        Assert.assertEquals(2, accounts.size());
        Assert.assertEquals("1", accounts.get(0).getBankKey());
    }

    @Test
    public void testCreateWrongType() {
        Assert.assertFalse(accountLogicFacade.create("p2", "SWEDBANK", "CREDITCARD"));
    }

    @Test
    public void testCreateUnknownPerson() {
        Assert.assertFalse(accountLogicFacade.create("nobody", "SWEDBANK", "CHECK"));
    }

    @Test
    public void testCreateUnknownBank() {
        Assert.assertFalse(accountLogicFacade.create("p2", "LEHMAN", "CHECK"));
        Assert.assertTrue(accountLogicFacade.findByPerson("p2").isEmpty());
    }
}
