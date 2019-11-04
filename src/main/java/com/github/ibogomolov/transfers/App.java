/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.ibogomolov.transfers;

import com.github.ibogomolov.transfers.datamodel.Account;
import com.github.ibogomolov.transfers.datastore.AppDatastore;
import com.github.ibogomolov.transfers.handler.CreateMoneyTransferHandler;
import com.github.ibogomolov.transfers.handler.GetAccountHandler;
import com.github.ibogomolov.transfers.handler.GetMoneyTransferHandler;
import lombok.extern.slf4j.Slf4j;

import static spark.Spark.get;
import static spark.Spark.post;

@Slf4j
public class App {

    private static final AppDatastore datastore = new AppDatastore();

    public static void main(String[] args) {
        datastore.accounts.insert(new Account("Isaac", "Newton", 1000.0));
        datastore.accounts.insert(new Account("Charles", "Darwin", 1000.0));
        datastore.accounts.insert(new Account("Harry", "Potter", 1000.0));

        post("/transfer", "application/json", new CreateMoneyTransferHandler(datastore));

        get("/account/:id", new GetAccountHandler(datastore));

        get("/transfer/:id", new GetMoneyTransferHandler(datastore));
    }
}
