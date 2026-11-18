package com.minibanking.banking.infrastructure.persistence;

import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.minibanking.banking.infrastructure.persistence.repository.SpringDataAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public class AccountPersistenceAdapter implements AccountRepository {
    private final SpringDataAccountRepository repository;
    private final AccountPersistenceMapper mapper;

    public AccountPersistenceAdapter(
            SpringDataAccountRepository repository,
            AccountPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        var entity = mapper.toEntity(account);

        var savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return repository.findById(accountId)
                .map(mapper::toDomain);
    }
}
