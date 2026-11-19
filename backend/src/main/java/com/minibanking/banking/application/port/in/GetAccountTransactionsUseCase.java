package com.minibanking.banking.application.port.in;

import java.util.UUID;

public interface GetAccountTransactionsUseCase {

    TransactionHistoryPage execute(
            UUID accountId,
            int page,
            int size
    );
}
