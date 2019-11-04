package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.Account;
import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateMoneyTransferHandlerMultithreadedAccessTest {

    private final long iterationsNumber = 1000;
    private final long threadCount = 1000;
    private final long accountBalance = iterationsNumber * threadCount;

    private final MoneyTransfer transfer = new MoneyTransfer(1L, 2L, 1.0, "");

    private final Account account1 = new Account("First1", "Last1", (double) accountBalance);
    private final Account account2 = new Account("First2", "Last2", 0.0);

    private final AppDatastore testDatastore = initTestDatastore();

    private AppDatastore initTestDatastore() {
        final AppDatastore datastore = new AppDatastore();
        datastore.accounts.insert(account1);
        datastore.accounts.insert(account2);
        return datastore;
    }

    private final CreateMoneyTransferHandler handler = new CreateMoneyTransferHandler(testDatastore);

    @Test
    public void testProcessMultithreaded() {
        final List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            final Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
        workers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        });

        assertEquals(accountBalance, testDatastore.accounts.findById(2L).getBalance(), 1E-6);
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < iterationsNumber; i++) {
                handler.process(transfer);
            }
        }
    }
}
