package com.minibanking.banking.application.usecase;

import com.minibanking.banking.application.exception.AccountNotFoundException;
import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.application.port.out.TransactionRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.AccountStatus;
import com.minibanking.banking.domain.model.Money;
import com.minibanking.banking.domain.model.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;

public class AccountUseCasesTest {

    private final AccountRepository accountRepository =
            mock(AccountRepository.class);

    private TransactionRepository transactionRepository =
            mock(TransactionRepository.class);

    @Test
    void shouldCreateAccount() {
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateAccountService service =
                new CreateAccountService(accountRepository);

        Account result = service.execute(Money.eur("100.00"));

        Assertions.assertNotNull(result.id());
        Assertions.assertEquals(Money.eur("100.00"), result.balance());
        Assertions.assertEquals(AccountStatus.ACTIVE, result.status());

        Mockito.verify(accountRepository).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void shouldDepositMoneyIntoAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId, Money.eur("100.00"));

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        DepositMoneyService depositMoneyService = new DepositMoneyService(
                accountRepository,
                transactionRepository
        );

        Account result = depositMoneyService.execute(accountId, Money.eur("50.00"));

        Assertions.assertEquals(Money.eur("150.00"), result.balance());
        Mockito.verify(accountRepository).save(account);
        verify(transactionRepository).save(
                argThat(transaction ->
                        transaction.type() == TransactionType.DEPOSIT
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenDepositingIntoUnknownAccount() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        DepositMoneyService depositMoneyService = new DepositMoneyService(
                accountRepository,
                transactionRepository
        );

        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> depositMoneyService.execute(accountId, Money.eur("50.00"))
        );

        Mockito.verify(accountRepository, never()).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void shouldWithdrawMoneyFromAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId, Money.eur("100.00"));

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        WithdrawMoneyService withdrawMoneyService = new WithdrawMoneyService(
                accountRepository,
                transactionRepository
        );

        Account result = withdrawMoneyService.execute(accountId, Money.eur("30.00"));

        Assertions.assertEquals(Money.eur("70.00"), result.balance());
        Mockito.verify(accountRepository).save(account);
        verify(transactionRepository).save(
                argThat(transaction ->
                        transaction.type() == TransactionType.WITHDRAWAL
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingFromUnknownAccount() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        WithdrawMoneyService withdrawMoneyService = new WithdrawMoneyService(
                accountRepository,
                transactionRepository
        );

        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> withdrawMoneyService.execute(accountId, Money.eur("30.00"))
        );

        Mockito.verify(accountRepository, never()).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void shouldBlockAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId, Money.eur("100.00"));

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        BlockAccountService service =
                new BlockAccountService(accountRepository);

        Account result = service.execute(accountId);

        Assertions.assertEquals(AccountStatus.BLOCKED, result.status());
        Mockito.verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowExceptionWhenBlockingUnknownAccount() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        BlockAccountService service =
                new BlockAccountService(accountRepository);

        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> service.execute(accountId)
        );

        Mockito.verify(accountRepository, never()).save(ArgumentMatchers.any(Account.class));
    }

    @Test
    void shouldUnblockAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId, Money.eur("100.00"));
        account.block();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        UnblockAccountService service =
                new UnblockAccountService(accountRepository);

        Account result = service.execute(accountId);

        Assertions.assertEquals(AccountStatus.ACTIVE, result.status());
        Mockito.verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowExceptionWhenUnblockingUnknownAccount() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        UnblockAccountService service =
                new UnblockAccountService(accountRepository);

        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> service.execute(accountId)
        );

        Mockito.verify(accountRepository, never()).save(ArgumentMatchers.any(Account.class));
    }

}
