package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
// import se.liu.ida.tdp024.account.util.logger.AccountLogger;
// import se.liu.ida.tdp024.account.util.logger.AccountLoggerImpl;



import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;



public class AccountEntityFacadeDB implements AccountEntityFacade {
    // private static final AccountLogger accountLogger = new AccountLoggerImpl();

    @Override
    public long createAccount(String personKey, String bankKey, String accountType) {
        
        EntityManager em = EMF.getEntityManager();

        try{
            em.getTransaction().begin();
            Account account = new AccountDB();
            account.setPersonKey(personKey);
            account.setBankKey(bankKey);
            account.setAccountType(accountType);
            account.setHoldingtransactions(0);
            em.persist(account);
            em.getTransaction().commit();

            return account.getId();
        }
        // catch(Exception e){
            // accountLogger.log(e);
            // return -1;
        // } 
        
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public Account find(long id) {
        EntityManager em = EMF.getEntityManager();
        try {
            return em.find(AccountDB.class, id);

        } 
        // catch (Exception e) {
            // accountLogger.log(e);
            // return null;
        // } 
        finally {
            em.close();
        }
    }

    @Override
    public List<Account> findPersonByKey(String personKey) {
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<AccountDB> query =
                em.createQuery(
                    "SELECT a FROM AccountDB a WHERE a.personKey = :personKey",
                    AccountDB.class
                );

            query.setParameter("personKey", personKey);
            return new ArrayList<Account>(query.getResultList());
        }
        // catch (Exception e) {
            // accountLogger.log(e);
            // return new ArrayList<Account>();
        // } 
        finally {
            em.close();
        }
    }

    @Override
    public boolean debitAccount(long accountId, long amount) {
        if (amount < 0) {
            return false;
        }
        return updateHoldings(accountId, -amount);
    }

    @Override
    public boolean creditAccount(long accountId, long amount) {
        if (amount < 0) {
            return false;
        }
        return updateHoldings(accountId, amount);
    }

    private Boolean updateHoldings(long id, long amount) {
        EntityManager em = EMF.getEntityManager();
        try {
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
            
            if (account == null) {
                return false;
            }

            long newHoldings = account.getHoldings() + amount;

            if (newHoldings < 0) {
                return false;
            }

            account.setHoldingtransactions(newHoldings);
            em.getTransaction().commit();

            return true;
        } 
        // catch (Exception e) {
            // accountLogger.log(e);
            // return false;
        // } 
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}