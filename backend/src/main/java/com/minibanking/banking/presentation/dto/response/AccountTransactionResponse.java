package com.minibanking.banking.presentation.dto.response;

import com.minibanking.banking.domain.model.AccountTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountTransactionResponse(
        UUID id,
        UUID accountId,
        String type,
        BigDecimal amount,
        String currency,
        LocalDateTime createdAt
) {
    public static AccountTransactionResponse from(
            AccountTransaction transaction
    ) {
        return new AccountTransactionResponse(
                transaction.id(),
                transaction.accountId(),
                transaction.type().name(),
                transaction.amount().amount(),
                transaction.amount().currency().name(),
                transaction.createdAt()
        );
    }
}
