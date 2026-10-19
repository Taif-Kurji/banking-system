package se.liu.ida.tdp024.account.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.util.kafka.EventPublisher;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountLogicFacade accountLogicFacade;
    private final TransactionLogicFacade transactionLogicFacade;
    private final EventPublisher eventPublisher;

    public AccountController(AccountLogicFacade accountLogicFacade, TransactionLogicFacade transactionLogicFacade, EventPublisher eventPublisher) {
        this.accountLogicFacade = accountLogicFacade;
        this.transactionLogicFacade = transactionLogicFacade;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping({"/create", "/create/"})
    public String create(@RequestParam(value = "accounttype", defaultValue = "") String accounttype, @RequestParam(value = "person", defaultValue = "") String person, @RequestParam(value = "bank", defaultValue = "") String bank) {
        eventPublisher.publish("rest-requests", "create accounttype=" + accounttype + " person=" + person + " bank=" + bank);
        return accountLogicFacade.create(person, bank, accounttype) ? "OK" : "FAILED";
    }

    @GetMapping("/find/person")
    public List<Account> findByPerson(@RequestParam(value = "person", defaultValue = "") String person) {
        eventPublisher.publish("rest-requests", "find/person person=" + person);
        return accountLogicFacade.findByPerson(person);
    }

    @GetMapping("/debit")
    public String debit(@RequestParam("id") long id, @RequestParam("amount") long amount) {
        eventPublisher.publish("rest-requests", "debit id=" + id + " amount=" + amount);
        return transactionLogicFacade.debit(id, amount) ? "OK" : "FAILED";
    }

    @GetMapping("/credit")
    public String credit(@RequestParam("id") long id, @RequestParam("amount") long amount) {
        eventPublisher.publish("rest-requests", "credit id=" + id + " amount=" + amount);
        return transactionLogicFacade.credit(id, amount) ? "OK" : "FAILED";
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions(@RequestParam("id") long id) {
        eventPublisher.publish("rest-requests", "transactions id=" + id);
        return transactionLogicFacade.findByAccount(id);
    }
}
