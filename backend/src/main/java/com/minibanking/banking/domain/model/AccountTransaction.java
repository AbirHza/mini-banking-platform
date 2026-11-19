package com.minibanking.banking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record AccountTransaction(UUID id,
                                 UUID accountId,
                                 TransactionType type,
                                 Money amount,
                                 LocalDateTime createdAt) {

    public AccountTransaction {
        Objects.requireNonNull(id, "Transaction ID is required");
        Objects.requireNonNull(accountId, "Account ID is required");
        Objects.requireNonNull(type, "Transaction type is required");
        Objects.requireNonNull(amount, "Transaction amount is required");
        Objects.requireNonNull(createdAt, "Creation date is required");

        if (amount.isZero()) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    public static AccountTransaction create(
            UUID accountId,
            TransactionType type,
            Money amount
    ) {
        return new AccountTransaction(
                UUID.randomUUID(),
                accountId,
                type,
                amount,
                LocalDateTime.now()
        );
    }
}
