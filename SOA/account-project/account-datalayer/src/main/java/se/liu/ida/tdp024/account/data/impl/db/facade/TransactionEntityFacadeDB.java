package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
// import se.liu.ida.tdp024.account.util.logger.AccountLogger;
// import se.liu.ida.tdp024.account.util.logger.AccountLoggerImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;



public class TransactionEntityFacadeDB implements TransactionEntityFacade {
    
    // private static final AccountLogger accountLogger = new AccountLoggerImpl();

    @Override
    public long createTransaction(String transactionType, long accountId, long amount, String status) {
        
        EntityManager em = EMF.getEntityManager();

        try{
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, accountId);

            if (account == null) {
                em.getTransaction().rollback();
                return -1;
            }
            Transaction transaction = new TransactionDB();
            transaction.setType(transactionType);
            transaction.setAmount(amount);
            transaction.setCreated(new Date());
            transaction.setAccount(account);
            transaction.setStatus(status);
            em.persist(transaction);
            em.getTransaction().commit();

            return transaction.getId();
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
    public List<Transaction> findTransactionsByAccountId(long accountId) {
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<TransactionDB> query = em.createQuery("SELECT t FROM TransactionDB t WHERE t.account.id = :accountId", TransactionDB.class);
            query.setParameter("accountId", accountId);
            return new ArrayList<>(query.getResultList());
        } 
        // catch (Exception e) {
            // accountLogger.log(e);
            // return new ArrayList<Transaction>();
        // } 
        finally {
            em.close();
        }
    }
}