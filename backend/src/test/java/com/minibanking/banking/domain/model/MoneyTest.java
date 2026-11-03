package com.minibanking.banking.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MoneyTest {

    @Test
    void shouldCreateMoneyInEuro() {
        Money money = Money.eur("100.00");

        Assertions.assertEquals(new BigDecimal("100.00"), money.amount());
        Assertions.assertEquals(Currency.EUR, money.currency());
    }

    @Test
    void shouldAddTwoMoneyAmounts() {
        Money first = Money.eur("100.00");
        Money second = Money.eur("50.00");

        Money result = first.add(second);

        Assertions.assertEquals(new BigDecimal("150.00"), result.amount());
    }

    @Test
    void shouldRejectNegativeAmount() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Money.eur("-10.00")
        );
    }

    @Test
    void shouldRejectMoreThanTwoDecimalPlaces() {
        Assertions.assertThrows(
                ArithmeticException.class,
                () -> Money.eur("10.123")
        );
    }

    @Test
    void shouldRejectSubtractionBelowZero() {
        Money first = Money.eur("50.00");
        Money second = Money.eur("100.00");

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> first.subtract(second)
        );
    }
}
