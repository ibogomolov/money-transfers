package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import lombok.val;
import org.eclipse.jetty.http.HttpStatus;
import org.javatuples.Pair;

public class CreateMoneyTransferHandler extends AbstractPostHandler<MoneyTransfer> {

    public CreateMoneyTransferHandler(AppDatastore datastore) {
        super(MoneyTransfer.class, datastore);
    }

    @Override
    protected Pair<Integer, String> process(MoneyTransfer transfer) {
        val fromAccount = datastore.accounts.findById(transfer.getFromAccountId());
        if (fromAccount == null) {
            return Pair.with(HttpStatus.BAD_REQUEST_400,
                    String.format("Account with id %d not found.", transfer.getFromAccountId()));
        }

        val toAccount = datastore.accounts.findById(transfer.getToAccountId());
        if (toAccount == null) {
            return Pair.with(HttpStatus.BAD_REQUEST_400,
                    String.format("Account with id %d not found.", transfer.getToAccountId()));
        }

        if (transfer.getAmount() <= 0) {
            return Pair.with(HttpStatus.BAD_REQUEST_400,
                    String.format("Cannot transfer non-positive amount %.1f.", transfer.getAmount()));
        }

        synchronized (datastore.accountsLock) {
            if (fromAccount.getBalance() < transfer.getAmount()) {
                return Pair.with(HttpStatus.FORBIDDEN_403,
                        String.format("Account with id %d doesn't have enough funds.", fromAccount.getId()));
            }

            fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());
        }

        datastore.moneyTransfers.insert(transfer);
        return Pair.with(HttpStatus.CREATED_201, "Money transferred successfully!");
    }
}
