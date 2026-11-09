package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.exception.AccountNotFoundException;
import com.minibanking.banking.application.port.in.UnblockAccountUseCase;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnblockAccountService implements UnblockAccountUseCase {

    private final AccountRepository accountRepository;

    public UnblockAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.unblock();

        return accountRepository.save(account);
    }
}
