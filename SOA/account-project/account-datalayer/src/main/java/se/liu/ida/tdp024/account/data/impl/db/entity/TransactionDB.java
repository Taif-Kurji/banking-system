package se.liu.ida.tdp024.account.data.impl.db.entity;
import  se.liu.ida.tdp024.account.data.api.entity.Transaction;
import  se.liu.ida.tdp024.account.data.api.entity.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;


@Entity 
public class TransactionDB implements Transaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @Column(nullable = false)
    private String type;
    private long amount;

    @Temporal (TemporalType.TIMESTAMP)
    @Column (nullable = false)
    private Date created;

    @Column (nullable = false)
    private String status;

    @ManyToOne(targetEntity = AccountDB.class, optional = false)
    private Account account;
    

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override 
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created; 
    }

    @Override 
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {      
        this.status = status;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }
}
