package com.minibanking.banking.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountTest {

        @Test
        void shouldCreateAccountWithInitialBalance() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            Assertions.assertEquals(Money.eur("100.00"), account.balance());
            Assertions.assertEquals(AccountStatus.ACTIVE, account.status());
            Assertions.assertNotNull(account.id());
            Assertions.assertNotNull(account.createdAt());
        }

        @Test
        void shouldDepositMoney() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.deposit(Money.eur("50.00"));

            Assertions.assertEquals(Money.eur("150.00"), account.balance());
        }

        @Test
        void shouldWithdrawMoney() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.withdraw(Money.eur("30.00"));

            Assertions.assertEquals(Money.eur("70.00"), account.balance());
        }

        @Test
        void shouldRejectWithdrawalWhenBalanceIsInsufficient() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("50.00")
            );

            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> account.withdraw(Money.eur("100.00"))
            );

            Assertions.assertEquals(Money.eur("50.00"), account.balance());
        }

        @Test
        void shouldRejectZeroDeposit() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> account.deposit(Money.eur("0.00"))
            );
        }

        @Test
        void shouldRejectZeroWithdrawal() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> account.withdraw(Money.eur("0.00"))
            );
        }

        @Test
        void shouldBlockAccount() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.block();

            Assertions.assertEquals(AccountStatus.BLOCKED, account.status());
        }

        @Test
        void shouldRejectDepositWhenAccountIsBlocked() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.block();

            Assertions.assertThrows(
                    IllegalStateException.class,
                    () -> account.deposit(Money.eur("50.00"))
            );
        }

        @Test
        void shouldRejectWithdrawalWhenAccountIsBlocked() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.block();

            Assertions.assertThrows(
                    IllegalStateException.class,
                    () -> account.withdraw(Money.eur("50.00"))
            );
        }

        @Test
        void shouldUnblockAccount() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.block();
            account.unblock();

            Assertions.assertEquals(AccountStatus.ACTIVE, account.status());
        }

        @Test
        void shouldAllowOperationsAfterUnblocking() {
            Account account = new Account(
                    UUID.randomUUID(),
                    Money.eur("100.00")
            );

            account.block();
            account.unblock();
            account.deposit(Money.eur("50.00"));

            Assertions.assertEquals(Money.eur("150.00"), account.balance());
        }

    @Test
    void shouldRejectAccountCreationWithNullId() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new Account(null, Money.eur("100.00"))
        );
    }

    @Test
    void shouldRejectAccountCreationWithNullBalance() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new Account(UUID.randomUUID(), null)
        );
    }

    @Test
    void shouldRejectDepositWithNullAmount() {
        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> account.deposit(null)
        );
    }

    @Test
    void shouldRejectWithdrawalWithNullAmount() {
        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> account.withdraw(null)
        );
    }

    @Test
    void shouldRejectDepositWithDifferentCurrency() {
        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        Money amountInAnotherCurrency = new Money(
                new BigDecimal("50.00"),
                Currency.USD
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(amountInAnotherCurrency)
        );
    }

    @Test
    void shouldRejectWithdrawalWithDifferentCurrency() {
        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        Money amountInAnotherCurrency = new Money(
                new BigDecimal("50.00"),
                Currency.USD
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(amountInAnotherCurrency)
        );
    }
}
