package com.github.ibogomolov.transfers.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.ibogomolov.transfers.datastore.Identifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransfer extends Identifiable {
    private final Long fromAccountId;
    private final Long toAccountId;
    private final Double amount;
    private final String message;

    @JsonCreator
    public MoneyTransfer(@JsonProperty(value = "fromAccountId", required = true) Long fromAccountId,
                         @JsonProperty(value = "toAccountId", required = true) Long toAccountId,
                         @JsonProperty(value = "amount", required = true) Double amount,
                         @JsonProperty(value = "message", required = true) String message) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.message = message;
    }
}
