package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.exception.AccountNotFoundException;
import com.minibanking.banking.application.port.in.BlockAccountUseCase;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class BlockAccountService implements BlockAccountUseCase {

    private final AccountRepository accountRepository;

    public BlockAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.block();

        return accountRepository.save(account);
    }
}
