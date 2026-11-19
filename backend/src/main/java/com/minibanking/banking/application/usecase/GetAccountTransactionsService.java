package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.exception.AccountNotFoundException;
import com.minibanking.banking.application.port.in.GetAccountTransactionsUseCase;
import com.minibanking.banking.application.port.in.TransactionHistoryPage;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.application.port.out.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAccountTransactionsService implements GetAccountTransactionsUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public GetAccountTransactionsService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionHistoryPage execute(
            UUID accountId,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        var transactions = transactionRepository.findByAccountId(
                accountId,
                page,
                size
        );

        long totalElements =
                transactionRepository.countByAccountId(accountId);

        int totalPages = (int) Math.ceil(
                (double) totalElements / size
        );

        return new TransactionHistoryPage(
                transactions,
                page,
                size,
                totalElements,
                totalPages
        );
    }
}
