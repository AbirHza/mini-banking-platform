package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.exception.AccountNotFoundException;
import com.minibanking.banking.application.port.in.DepositMoneyUseCase;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.application.port.out.TransactionRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.AccountTransaction;
import com.minibanking.banking.domain.model.Money;
import com.minibanking.banking.domain.model.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DepositMoneyService implements DepositMoneyUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public DepositMoneyService(AccountRepository accountRepository,TransactionRepository transactionRepository ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Account execute(UUID accountId, Money amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.deposit(amount);

        Account savedAccount = accountRepository.save(account);

        AccountTransaction transaction = AccountTransaction.create(
                accountId,
                TransactionType.DEPOSIT,
                amount
        );

        transactionRepository.save(transaction);

        return savedAccount;
    }
}
