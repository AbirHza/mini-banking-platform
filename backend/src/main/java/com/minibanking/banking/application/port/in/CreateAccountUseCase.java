package com.minibanking.banking.application.port.in;

import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.Money;

public interface CreateAccountUseCase {

    Account execute(Money initialBalance);
}
