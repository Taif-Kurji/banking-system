package se.liu.ida.tdp024.account.data.test.facade;


import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;



public class TransactionEntityFacadeTest {
    
    //---- Unit under test ----//
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(); 
    private TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    private StorageFacade storageFacade;

    
    @After
    public void tearDown() {
    // storageFacade.emptyStorage();
    }
   
    @Test 
    public void testCreateTransaction() {
        long accountId = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        long transactionId = transactionEntityFacade.createTransaction("deposit", accountId, 1000, "success");
        Assert.assertTrue(transactionId > 0);
        
        List<Transaction> transactions = transactionEntityFacade.findTransactionsByAccountId(accountId);
        
        Transaction transaction = transactions.get(0);
        
        Assert.assertNotNull(transactions);
        Assert.assertEquals(1, transactions.size());
        Assert.assertEquals("deposit", transaction.getType());
        Assert.assertEquals(accountId, transaction.getAccount().getId());
        Assert.assertEquals(1000, transaction.getAmount());
        Assert.assertNotNull(transaction.getCreated());
        Assert.assertEquals("success", transaction.getStatus());
        Assert.assertEquals(transactionId, transaction.getId());
    
    }

    @Test 
    public void testFailedTransactionIsStored() {
        long accountId = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        long transactionId = transactionEntityFacade.createTransaction("withdrawal", accountId, 5000, "failed");
        Assert.assertTrue(transactionId > 0);
        
        List<Transaction> transactions = transactionEntityFacade.findTransactionsByAccountId(accountId);
        
        Transaction transaction = transactions.get(0);
        Assert.assertEquals(1, transactions.size());
        Assert.assertEquals("failed", transaction.getStatus());
    }

    @Test 
    public void testTransactionToAccounts() {
        long accountId1 = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        long accountId2 = accountEntityFacade.createAccount("abdi", "bank2", "savings");
        
        long transactionId1 = transactionEntityFacade.createTransaction("deposit", accountId1, 1000, "success");
        long transactionId2 = transactionEntityFacade.createTransaction("deposit", accountId2, 2000, "success");
        long transactionId3 = transactionEntityFacade.createTransaction("withdrawal", accountId1, 500, "success");
        
        List<Transaction> transactions1 = transactionEntityFacade.findTransactionsByAccountId(accountId1);
        List<Transaction> transactions2 = transactionEntityFacade.findTransactionsByAccountId(accountId2);
        
        Assert.assertEquals(2, transactions1.size());
        Assert.assertEquals(1, transactions2.size());
        
        Transaction transaction1 = transactions1.get(0);
        Transaction transaction2 = transactions2.get(0);
        
        Assert.assertEquals(accountId1, transaction1.getAccount().getId());
        Assert.assertEquals(accountId2, transaction2.getAccount().getId());
    }

    @Test 
    public void testFindWithoutTransactions() {
        long accountId = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        List<Transaction> transactions = transactionEntityFacade.findTransactionsByAccountId(accountId);

        Assert.assertNotNull(transactions);
        Assert.assertTrue(transactions.isEmpty());
    }    

    @Test 
    public void testCreateUnknownAccount() {
        Assert.assertEquals(-1, transactionEntityFacade.createTransaction("deposit", 9999999, 1000, "failed"));
    }


}