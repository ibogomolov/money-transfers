package com.github.ibogomolov.transfers.datastore;

import com.github.ibogomolov.transfers.datamodel.Account;
import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;

public class AppDatastore {
    public final InMemoryTable<Account> accounts = new InMemoryTable<>();
    public final Object accountsLock = new Object();
    public final InMemoryTable<MoneyTransfer> moneyTransfers = new InMemoryTable<>();
}
