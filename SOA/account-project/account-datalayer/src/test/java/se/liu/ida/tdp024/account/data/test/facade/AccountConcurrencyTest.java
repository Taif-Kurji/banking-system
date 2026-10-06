package se.liu.ida.tdp024.account.data.test.facade;


import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;


public class AccountConcurrencyTest{

    private static final int OPERATIONS = 100;
    private static final int THREADS = 10;

    //---- Unit under test ----//
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(); 
    private StorageFacade storageFacade = new StorageFacadeDB();

    
    @After
    public void tearDown() {
    // storageFacade.emptyStorage();
    }

    @Test 
    public void testConcurrentDebitAccountEmptiesAccountExactly() throws Exception {
        final long accountId = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        long initialAmount = (long) (Math.random() * 100); 

        accountEntityFacade.creditAccount(accountId, initialAmount);

        runConcurrently(new Runnable() {
            @Override
            public void run() {
                accountEntityFacade.debitAccount(accountId, (long) (Math.random() * 10));
            }
        });


        
        Account account = accountEntityFacade.find(accountId);
        Assert.assertEquals(0, account.getHoldings());
    }
    @Test 
    public void testConcurrentCreditAccountLossNoUpdates() throws Exception {
        final long accountId = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        long initialAmount = 100; 

        accountEntityFacade.creditAccount(accountId, initialAmount);

        runConcurrently(new Runnable() {
            @Override
            public void run() {
                accountEntityFacade.creditAccount(accountId,initialAmount);
            }
        });

        
        Account account = accountEntityFacade.find(accountId);
        Assert.assertEquals((OPERATIONS + 1) * initialAmount, account.getHoldings());
    }

    private void runConcurrently(Runnable operation) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < OPERATIONS; i++) {
            executor.submit(operation);
        }
        executor.shutdown();
        Assert.assertTrue("Opernatorn hann inte slutförs inom tidsgräns",executor.awaitTermination(120, TimeUnit.SECONDS));
    }



}