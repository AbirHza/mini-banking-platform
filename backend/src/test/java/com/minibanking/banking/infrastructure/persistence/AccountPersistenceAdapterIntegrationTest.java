package com.minibanking.banking.infrastructure.persistence;

import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.AccountStatus;
import com.minibanking.banking.domain.model.Money;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class AccountPersistenceAdapterIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldSaveAndRetrieveAccount() {

        // Arrange
        UUID accountId = UUID.randomUUID();

        Account account = new Account(
                accountId,
                Money.eur("150.00")
        );

        // Act
        accountRepository.save(account);

        Optional<Account> result =
                accountRepository.findById(accountId);

        // Assert
        Assertions.assertTrue(result.isPresent());

        Account retrievedAccount = result.orElseThrow();

        Assertions.assertEquals(accountId, retrievedAccount.id());
        Assertions.assertEquals(Money.eur("150.00"), retrievedAccount.balance());
        Assertions.assertEquals(AccountStatus.ACTIVE, retrievedAccount.status());
        Assertions.assertEquals(account.createdAt(), retrievedAccount.createdAt());
    }

    @Test
    void shouldPersistBlockedAccount() {

        // Arrange
        UUID accountId = UUID.randomUUID();

        Account account = new Account(
                accountId,
                Money.eur("300.00")
        );

        account.block();

        // Act
        accountRepository.save(account);

        Optional<Account> result =
                accountRepository.findById(accountId);

        // Assert
        Assertions.assertTrue(result.isPresent());

        Account retrievedAccount = result.orElseThrow();

        Assertions.assertEquals(AccountStatus.BLOCKED, retrievedAccount.status());
        Assertions.assertEquals(Money.eur("300.00"), retrievedAccount.balance());
    }

    @Test
    void shouldReturnEmptyWhenAccountDoesNotExist() {

        // Arrange
        UUID unknownAccountId = UUID.randomUUID();

        // Act
        Optional<Account> result =
                accountRepository.findById(unknownAccountId);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }
}
