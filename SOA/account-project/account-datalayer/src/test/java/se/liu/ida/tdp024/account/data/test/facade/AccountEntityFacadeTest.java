package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;


public class AccountEntityFacadeTest {
    
    //---- Unit under test ----//
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(); 
    private StorageFacade storageFacade;

    
    @After
    public void tearDown() {
    // storageFacade.emptyStorage();
    }
    
    @Test
    public void testCreate() {
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        Account account = accountEntityFacade.find(id);

        Assert.assertNotNull(account);
        Assert.assertEquals("abdi", account.getPersonKey());
        Assert.assertEquals("bank1", account.getBankKey());
        Assert.assertEquals("checking" , account.getAccountType());

        Assert.assertEquals(0, account.getHoldings());



    }
    @Test 
    public void testCreateDuplicateAccount() {

       long firstId =  accountEntityFacade.createAccount("abdi", "bank1", "checking");
       long secondId = accountEntityFacade.createAccount("abdi", "bank1", "checking");

        assertTrue(firstId > 0);
        assertTrue(secondId > 0);
        assertNotEquals(firstId, secondId);

    }

    @Test 
     public void testFindUnknownId() {
        Account account = accountEntityFacade.find(9999999);
        Assert.assertNull(account);
    }

    @Test
    public void findPersonByKeyTest(){
        List <Account> account = accountEntityFacade.findPersonByKey( "404");
        Assert.assertNotNull(account);
        assertTrue(account.isEmpty());
    }

    @Test 
    public void TestCreditAccount(){
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        Assert.assertTrue(accountEntityFacade.creditAccount(id, 100));
        Assert.assertEquals(100, accountEntityFacade.find(id).getHoldings());

        Assert.assertTrue(accountEntityFacade.creditAccount(id, 100));
        Assert.assertEquals(200, accountEntityFacade.find(id).getHoldings());

    }

    @Test 
    public void TestDebitAccount(){
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        accountEntityFacade.creditAccount(id, 100);

        Assert.assertTrue(accountEntityFacade.debitAccount(id, 50));
        Assert.assertEquals(50, accountEntityFacade.find(id).getHoldings());
    }

    @Test 
    public void DebitAccountEmptyAccount(){
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        Assert.assertFalse(accountEntityFacade.debitAccount(id, 50));
        Assert.assertEquals(0, accountEntityFacade.find(id).getHoldings());
    }

    @Test 
    public void DebitAccountInsufficientFunds(){
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        accountEntityFacade.creditAccount(id, 50);
        Assert.assertFalse(accountEntityFacade.debitAccount(id, 100));
        Assert.assertEquals(50, accountEntityFacade.find(id).getHoldings());
    }
    @Test 
    public void DebitAccountAndCreditUnknownAccount(){
        Assert.assertFalse(accountEntityFacade.debitAccount(9999999, 100));
        Assert.assertFalse(accountEntityFacade.creditAccount(9999999, 100));
    }
    @Test 
    public void DebitAccountAndCreditNegativeAmount(){
        long id = accountEntityFacade.createAccount("abdi", "bank1", "checking");
        Assert.assertFalse(accountEntityFacade.debitAccount(id, -100));
        Assert.assertFalse(accountEntityFacade.creditAccount(id, -100));
    }



}