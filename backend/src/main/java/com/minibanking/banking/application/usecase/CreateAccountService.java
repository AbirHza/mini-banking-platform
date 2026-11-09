package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.port.in.CreateAccountUseCase;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.Money;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepository accountRepository;

    public CreateAccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(Money initialBalance) {
        Account account = new Account(
                UUID.randomUUID(),
                initialBalance
        );

        return accountRepository.save(account);
    }

}
