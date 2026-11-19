package com.minibanking.banking.infrastructure.persistence;

import com.minibanking.banking.application.port.out.TransactionRepository;
import com.minibanking.banking.domain.model.AccountTransaction;
import com.minibanking.banking.infrastructure.persistence.mapper.AccountTransactionPersistenceMapper;
import com.minibanking.banking.infrastructure.persistence.repository.SpringDataAccountTransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AccountTransactionPersistenceAdapter implements TransactionRepository {

    private final SpringDataAccountTransactionRepository repository;
    private final AccountTransactionPersistenceMapper mapper;

    public AccountTransactionPersistenceAdapter(
            SpringDataAccountTransactionRepository repository,
            AccountTransactionPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AccountTransaction save(AccountTransaction transaction) {
        var entity = mapper.toEntity(transaction);

        var savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<AccountTransaction> findByAccountId(
            UUID accountId,
            int page,
            int size
    ) {
        return repository
                .findByAccountIdOrderByCreatedAtDesc(
                        accountId,
                        PageRequest.of(page, size)
                )
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByAccountId(UUID accountId) {
        return repository.countByAccountId(accountId);
    }
}
