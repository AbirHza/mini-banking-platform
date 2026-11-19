package com.minibanking.banking.presentation.controller;

import com.minibanking.banking.application.port.in.*;
import com.minibanking.banking.domain.model.Money;
import com.minibanking.banking.presentation.dto.request.CreateAccountRequest;
import com.minibanking.banking.presentation.dto.request.MoneyOperationRequest;
import com.minibanking.banking.presentation.dto.response.AccountResponse;
import com.minibanking.banking.application.port.in.GetAccountTransactionsUseCase;
import com.minibanking.banking.presentation.dto.response.TransactionHistoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final BlockAccountUseCase blockAccountUseCase;
    private final UnblockAccountUseCase unblockAccountUseCase;
    private final GetAccountTransactionsUseCase getAccountTransactionsUseCase;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            GetAccountUseCase getAccountUseCase,
            DepositMoneyUseCase depositMoneyUseCase,
            WithdrawMoneyUseCase withdrawMoneyUseCase,
            BlockAccountUseCase blockAccountUseCase,
            UnblockAccountUseCase unblockAccountUseCase,
            GetAccountTransactionsUseCase getAccountTransactionsUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.depositMoneyUseCase = depositMoneyUseCase;
        this.withdrawMoneyUseCase = withdrawMoneyUseCase;
        this.blockAccountUseCase = blockAccountUseCase;
        this.unblockAccountUseCase = unblockAccountUseCase;
        this.getAccountTransactionsUseCase = getAccountTransactionsUseCase;
    }
    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        var account = createAccountUseCase.execute(
                Money.eur(request.initialBalance().toPlainString())
        );

        return ResponseEntity
                .created(URI.create("/api/accounts/" + account.id()))
                .body(AccountResponse.from(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                AccountResponse.from(getAccountUseCase.execute(id))
        );
    }

    @PostMapping("/{id}/deposits")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable UUID id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        var account = depositMoneyUseCase.execute(
                id, Money.eur(request.amount().toPlainString())
        );
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/{id}/withdrawals")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable UUID id,
            @Valid @RequestBody MoneyOperationRequest request
    ) {
        var account = withdrawMoneyUseCase.execute(
                id, Money.eur(request.amount().toPlainString())
        );
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<AccountResponse> block(@PathVariable UUID id) {
        return ResponseEntity.ok(
                AccountResponse.from(blockAccountUseCase.execute(id))
        );
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<AccountResponse> unblock(@PathVariable UUID id) {
        return ResponseEntity.ok(
                AccountResponse.from(unblockAccountUseCase.execute(id))
        );
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<TransactionHistoryResponse> getTransactions(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = getAccountTransactionsUseCase.execute(id, page, size);

        return ResponseEntity.ok(
                TransactionHistoryResponse.from(result)
        );
    }

}
