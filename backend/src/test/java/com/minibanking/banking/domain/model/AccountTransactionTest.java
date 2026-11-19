package com.minibanking.banking.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AccountTransactionTest {

    @Test
    void shouldCreateDepositTransaction() {
        UUID accountId = UUID.randomUUID();

        AccountTransaction transaction = AccountTransaction.create(
                accountId,
                TransactionType.DEPOSIT,
                Money.eur("100.00")
        );

        Assertions.assertNotNull(transaction.id());
        Assertions.assertEquals(accountId, transaction.accountId());
        Assertions.assertEquals(TransactionType.DEPOSIT, transaction.type());
        Assertions.assertEquals(Money.eur("100.00"), transaction.amount());
        Assertions.assertNotNull(transaction.createdAt());
    }

    @Test
    void shouldCreateWithdrawalTransaction() {
        AccountTransaction transaction = AccountTransaction.create(
                UUID.randomUUID(),
                TransactionType.WITHDRAWAL,
                Money.eur("50.00")
        );

        Assertions.assertEquals(
                TransactionType.WITHDRAWAL,
                transaction.type()
        );
    }

    @Test
    void shouldRejectZeroAmount() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> AccountTransaction.create(
                        UUID.randomUUID(),
                        TransactionType.DEPOSIT,
                        Money.eur("0.00")
                )
        );
    }
}
