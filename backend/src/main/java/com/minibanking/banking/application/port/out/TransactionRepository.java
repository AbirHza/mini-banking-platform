package com.minibanking.banking.application.port.out;

import com.minibanking.banking.domain.model.AccountTransaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    AccountTransaction save(AccountTransaction transaction);

    List<AccountTransaction> findByAccountId(
            UUID accountId,
            int page,
            int size
    );

    long countByAccountId(UUID accountId);
}
