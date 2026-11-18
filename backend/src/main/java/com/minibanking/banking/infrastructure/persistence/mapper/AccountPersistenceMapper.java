package com.minibanking.banking.infrastructure.persistence.mapper;

import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.Money;
import com.minibanking.banking.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {

    public AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.id(),
                account.balance().amount(),
                account.balance().currency(),
                account.status(),
                account.createdAt()
        );
    }

    public Account toDomain(AccountEntity entity) {
        Money balance = new Money(
                entity.getBalance(),
                entity.getCurrency()
        );

        return Account.reconstitute(
                entity.getId(),
                balance,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
