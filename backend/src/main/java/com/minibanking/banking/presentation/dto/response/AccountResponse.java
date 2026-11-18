package com.minibanking.banking.presentation.dto.response;

import com.minibanking.banking.domain.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(UUID id,
                              BigDecimal balance,
                              String currency,
                              String status,
                              LocalDateTime createdAt) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.id(),
                account.balance().amount(),
                account.balance().currency().name(),
                account.status().name(),
                account.createdAt()
        );
    }
}
