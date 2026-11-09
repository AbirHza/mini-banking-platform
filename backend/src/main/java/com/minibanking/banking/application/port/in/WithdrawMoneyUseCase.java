package com.minibanking.banking.application.port.in;

import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.Money;

import java.util.UUID;

public interface WithdrawMoneyUseCase {

    Account execute(UUID accountId, Money amount);
}
