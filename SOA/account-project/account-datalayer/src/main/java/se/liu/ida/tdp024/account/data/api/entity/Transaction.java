package se.liu.ida.tdp024.account.data.api.entity;
import java.io.Serializable;
import java.util.Date;

public interface Transaction extends Serializable {
    
    long getId();
    void setId(long id);

    String getType();
    void setType(String type);

    long getAmount();
    void setAmount(long amount);

    Date getCreated();
    void setCreated(Date created);

    String getStatus();
    void setStatus(String status);

    Account getAccount();
    void setAccount(Account account);
    
}