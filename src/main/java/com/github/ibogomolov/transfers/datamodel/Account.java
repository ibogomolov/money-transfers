package com.github.ibogomolov.transfers.datamodel;

import com.github.ibogomolov.transfers.datastore.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Account extends Identifiable {
    private final String firstName;
    private final String lastName;
    private Double balance;
}
