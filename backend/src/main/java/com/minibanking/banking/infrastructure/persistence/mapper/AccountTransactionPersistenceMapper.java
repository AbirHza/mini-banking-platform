package com.minibanking.banking.infrastructure.persistence.mapper;

import com.minibanking.banking.domain.model.AccountTransaction;
import com.minibanking.banking.domain.model.Money;
import com.minibanking.banking.infrastructure.persistence.entity.AccountTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionPersistenceMapper {

    public AccountTransactionEntity toEntity(
            AccountTransaction transaction
    ) {
        return new AccountTransactionEntity(
                transaction.id(),
                transaction.accountId(),
                transaction.type(),
                transaction.amount().amount(),
                transaction.amount().currency().name(),
                transaction.createdAt()
        );
    }

    public AccountTransaction toDomain(
            AccountTransactionEntity entity
    ) {
        Money amount = new Money(
                entity.getAmount(),
                com.minibanking.banking.domain.model.Currency.valueOf(
                        entity.getCurrency()
                )
        );

        return new AccountTransaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getType(),
                amount,
                entity.getCreatedAt()
        );
    }
}
