package com.minibanking.banking.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount is required");
        Objects.requireNonNull(currency, "Currency is required");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Money amount cannot be negative"
            );
        }

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);
    }

    public static Money eur(String amount) {
        return new Money(new BigDecimal(amount), Currency.EUR);
    }
    public boolean isZero() {
        return amount.signum() == 0;
    }

    public Money add(Money other) {
        checkSameCurrency(other);

        return new Money(
                amount.add(other.amount),
                currency
        );
    }

    public Money subtract(Money other) {
        checkSameCurrency(other);

        BigDecimal result = amount.subtract(other.amount);

        if (result.signum() < 0) {
            throw new IllegalArgumentException(
                    "Money amount cannot become negative"
            );
        }

        return new Money(result, currency);
    }

    private void checkSameCurrency(Money other) {
        Objects.requireNonNull(other, "Money is required");

        if (currency != other.currency) {
            throw new IllegalArgumentException(
                    "Currencies must match"
            );
        }
    }
}