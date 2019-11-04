package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.Account;
import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import org.javatuples.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateMoneyTransferHandlerTest {

    private final MoneyTransfer invalidTransfer1 = new MoneyTransfer(-1L, 1L, 10.0, "message");
    private final MoneyTransfer invalidTransfer2 = new MoneyTransfer(1L, 0L, 10.0, "message");
    private final MoneyTransfer invalidTransfer3 = new MoneyTransfer(1L, 2L, -10.0, "message");
    private final MoneyTransfer validTransfer1 = new MoneyTransfer(1L, 2L, 10.0, "");

    private final Account account1 = new Account("First1", "Last1", 100.0);
    private final Account account2 = new Account("First2", "Last2", 100.0);

    private final AppDatastore testDatastore = initTestDatastore();

    private AppDatastore initTestDatastore() {
        final AppDatastore datastore = new AppDatastore();
        datastore.accounts.insert(account1);
        datastore.accounts.insert(account2);
        return datastore;
    }

    private final CreateMoneyTransferHandler handler = new CreateMoneyTransferHandler(testDatastore);

    @Test
    public void testProcessNegative() {
        assertEquals(Pair.with(400, "Account with id -1 not found."), handler.process(invalidTransfer1));
        assertEquals(Pair.with(400, "Account with id 0 not found."), handler.process(invalidTransfer2));
        assertEquals(Pair.with(400, "Cannot transfer non-positive amount -10.0."), handler.process(invalidTransfer3));
    }

    @Test
    public void testProcessPositive() {
        assertEquals(Pair.with(201, "Money transferred successfully!"), handler.process(validTransfer1));
        assertEquals(90.0, testDatastore.accounts.findById(1L).getBalance(), 1E-6);
        assertEquals(110.0, testDatastore.accounts.findById(2L).getBalance(), 1E-6);
    }
}