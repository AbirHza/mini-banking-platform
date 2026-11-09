package com.minibanking.banking.application.port.out;

import com.minibanking.banking.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID accountId);
}
