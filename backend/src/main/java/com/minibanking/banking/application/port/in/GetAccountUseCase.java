package com.minibanking.banking.application.port.in;

import com.minibanking.banking.domain.model.Account;

import java.util.UUID;

public interface GetAccountUseCase {
    Account execute(UUID accountId);

}
