package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
// import se.liu.ida.tdp024.account.util.logger.AccountLogger;
// import se.liu.ida.tdp024.account.util.logger.AccountLoggerImpl;



import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;



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
        return updateHoldings(accountId, amount, "DEBIT");
    }

    @Override
    public boolean creditAccount(long accountId, long amount) {
        return updateHoldings(accountId, amount, "CREDIT");
    }

    private boolean updateHoldings(long id, long amount, String type) {
        EntityManager em = EMF.getEntityManager();
        try {
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);

            if (account == null) {
                return false;
            }

            long newHoldings;
            if (type.equals("DEBIT")) {
                newHoldings = account.getHoldings() - amount;
            } else {
                newHoldings = account.getHoldings() + amount;
            }

            boolean ok = amount >= 0 && newHoldings >= 0;
            if (ok) {
                account.setHoldingtransactions(newHoldings);
            }

            Transaction transaction = new TransactionDB();
            transaction.setType(type);
            transaction.setAmount(amount);
            transaction.setCreated(new Date());
            transaction.setStatus(ok ? "OK" : "FAILED");
            transaction.setAccount(account);
            em.persist(transaction);

            em.getTransaction().commit();

            return ok;
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