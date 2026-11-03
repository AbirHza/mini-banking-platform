package com.minibanking.banking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final UUID id;
    private Money balance;
    private AccountStatus status;
    private final LocalDateTime createdAt;


    public Account(UUID id, Money initialBalance) {
        this.id = Objects.requireNonNull(id, "Account ID is required");
        this.balance = Objects.requireNonNull(initialBalance, "Initial balance is required");
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void deposit(Money amount){
        ensureAccountIsActive();
        validatePositiveAmount(amount);

        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount){
        ensureAccountIsActive();
        validatePositiveAmount(amount);

        this.balance = this.balance.subtract(amount);
    }

    public void block(){
        this.status = AccountStatus.BLOCKED;
    }

    public void unblock(){
        this.status = AccountStatus.ACTIVE;
    }

    private void ensureAccountIsActive() {
        if (status == AccountStatus.BLOCKED) {
            throw new IllegalStateException(
                    "Cannot perform operations on a blocked account"
            );
        }
    }

    private void validatePositiveAmount(Money amount) {
        Objects.requireNonNull(amount, "Amount is required");

        if (amount.isZero()) {
            throw new IllegalArgumentException(
                    "Operation amount must be greater than zero"
            );
        }
    }

    public UUID id() {
        return id;
    }

    public Money balance() {
        return balance;
    }

    public AccountStatus status() {
        return status;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }
}
