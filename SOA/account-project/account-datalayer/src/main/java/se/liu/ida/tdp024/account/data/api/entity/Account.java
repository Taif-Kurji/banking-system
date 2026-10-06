package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;
public interface Account extends Serializable {
    long getId();
    void setId(long id);

    String getPersonKey();
    void setPersonKey(String personKey);

    String getBankKey();
    void setBankKey(String bankKey);

    String getAccountType();
    void setAccountType(String accountType);

    long getHoldings();
    void setHoldingtransactions(long holdings);
} 
