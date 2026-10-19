package se.liu.ida.tdp024.account.logic.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.test.util.FakeEventPublisher;

public class TransactionLogicFacadeTest {

    private TransactionLogicFacade transactionLogicFacade;
    private AccountEntityFacade accountEntityFacade;
    private FakeEventPublisher eventPublisher;
    private StorageFacade storageFacade;

    @Before
    public void setUp() {
        accountEntityFacade = new AccountEntityFacadeDB();
        eventPublisher = new FakeEventPublisher();
        transactionLogicFacade = new TransactionLogicFacadeImpl(new TransactionEntityFacadeDB(), accountEntityFacade, eventPublisher);
        storageFacade = new StorageFacadeDB();
    }

    @After
    public void tearDown() {
        storageFacade.emptyStorage();
    }

    @Test
    public void testCreditAndDebit() {
        long id = accountEntityFacade.createAccount("t1", "1", "CHECK");

        Assert.assertTrue(transactionLogicFacade.credit(id, 100));
        Assert.assertTrue(transactionLogicFacade.debit(id, 30));
        Assert.assertEquals(70, accountEntityFacade.find(id).getHoldings());
        Assert.assertEquals(2, transactionLogicFacade.findByAccount(id).size());
        Assert.assertEquals(2, eventPublisher.messages.size());
    }

    @Test
    public void testDebitTooMuch() {
        long id = accountEntityFacade.createAccount("t2", "1", "SAVINGS");

        Assert.assertFalse(transactionLogicFacade.debit(id, 1000));

        List<Transaction> transactions = transactionLogicFacade.findByAccount(id);
        Assert.assertEquals(1, transactions.size());
        Assert.assertEquals("FAILED", transactions.get(0).getStatus());
    }
}
