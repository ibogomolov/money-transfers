package com.github.ibogomolov.transfers.handler;

import com.github.ibogomolov.transfers.datamodel.MoneyTransfer;
import com.github.ibogomolov.transfers.datastore.AppDatastore;

public class GetMoneyTransferHandler extends AbstractGetHandler<MoneyTransfer> {

    public GetMoneyTransferHandler(AppDatastore datastore) {
        super(datastore);
    }

    @Override
    protected MoneyTransfer retrieve(Long id) {
        return datastore.moneyTransfers.findById(id);
    }
}
