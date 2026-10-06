package se.liu.ida.tdp024.account.data.impl.db.entity;
import se.liu.ida.tdp024.account.data.api.entity.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AccountDB implements Account {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String personKey;
    @Column(nullable = false)
    private String bankKey;
    @Column(nullable = false)
    private String accountType;
    private long holdings;

    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getPersonKey() {
        return personKey;
    }

    @Override
    public void setPersonKey(String personKey) {
        this.personKey = personKey;
    }

    @Override
    public String getBankKey() {
        return bankKey;
    }

    @Override
    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public long getHoldings() {
        return holdings;
    }

    @Override
    public void setHoldingtransactions(long holdings) {
        this.holdings = holdings;
    }
}
