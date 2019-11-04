package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.Account;
import com.github.ibogomolov.transfers.datastore.AppDatastore;

public class GetAccountHandler extends AbstractGetHandler<Account> {

    public GetAccountHandler(AppDatastore datastore) {
        super(datastore);
    }

    @Override
    protected Account retrieve(Long id) {
        return datastore.accounts.findById(id);
    }
}
