package com.github.ibogomolov.transfers.datamodel;

import com.fasterxml.jackson.annotation.*;
import com.github.ibogomolov.transfers.datastore.Identifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransfer extends Identifiable {
    @JsonSetter(nulls = Nulls.FAIL)
    private final Long fromAccountId;
    @JsonSetter(nulls = Nulls.FAIL)
    private final Long toAccountId;
    @JsonSetter(nulls = Nulls.FAIL)
    private final Double amount;
    @JsonSetter(nulls = Nulls.FAIL)
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
