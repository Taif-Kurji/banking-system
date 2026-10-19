package se.liu.ida.tdp024.account.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.util.kafka.EventPublisher;

class AccountControllerTest {

    private final List<String> published = new ArrayList<>();
    private AccountController controller;

    @BeforeEach
    void setUp() {
        AccountLogicFacade accounts = new AccountLogicFacade() {
            public boolean create(String personKey, String bankName, String accountType) {
                return "CHECK".equals(accountType);
            }
            public List<Account> findByPerson(String personKey) {
                return new ArrayList<>();
            }
        };
        TransactionLogicFacade transactions = new TransactionLogicFacade() {
            public boolean debit(long id, long amount) {
                return amount <= 10;
            }
            public boolean credit(long id, long amount) {
                return amount > 0;
            }
            public List<Transaction> findByAccount(long id) {
                return new ArrayList<>();
            }
        };
        EventPublisher publisher = (topic, message) -> published.add(topic + ":" + message);
        controller = new AccountController(accounts, transactions, publisher);
    }

    @Test
    void testCreate() {
        assertEquals("OK", controller.create("CHECK", "3", "SWEDBANK"));
        assertEquals("FAILED", controller.create("CREDITCARD", "3", "SWEDBANK"));
    }

    @Test
    void testDebitAndCredit() {
        assertEquals("OK", controller.debit(1, 5));
        assertEquals("FAILED", controller.debit(1, 50));
        assertEquals("OK", controller.credit(1, 5));
        assertEquals("FAILED", controller.credit(1, -5));
    }

    @Test
    void testFind() {
        assertTrue(controller.findByPerson("3").isEmpty());
        assertTrue(controller.transactions(1).isEmpty());
    }

    @Test
    void testPublish() {
        controller.create("CHECK", "3", "SWEDBANK");
        controller.findByPerson("3");
        controller.debit(1, 1);
        controller.credit(1, 1);
        controller.transactions(1);
        assertEquals(5, published.size());
        assertTrue(published.stream().allMatch(m -> m.startsWith("rest-requests:")));
    }
}
