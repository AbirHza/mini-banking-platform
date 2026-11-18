package com.minibanking.banking.presentation.controller;

import com.minibanking.banking.application.port.out.AccountRepository;
import com.minibanking.banking.domain.model.Account;
import com.minibanking.banking.domain.model.Money;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldCreateAccount() throws Exception {

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "initialBalance": 150.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.balance").value(150.00))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldRejectNegativeInitialBalance() throws Exception {

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "initialBalance": -50.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist() throws Exception {

        UUID unknownId = UUID.randomUUID();

        mockMvc.perform(get("/api/accounts/{id}", unknownId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldDepositMoney() throws Exception {

        Account account = accountRepository.save(
                new Account(UUID.randomUUID(), Money.eur("100.00"))
        );

        mockMvc.perform(post("/api/accounts/{id}/deposits", account.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 50.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.00));
    }

    @Test
    void shouldWithdrawMoney() throws Exception {

        Account account = accountRepository.save(
                new Account(UUID.randomUUID(), Money.eur("100.00"))
        );

        mockMvc.perform(post("/api/accounts/{id}/withdrawals", account.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 30.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(70.00));
    }

    @Test
    void shouldBlockAccount() throws Exception {

        Account account = accountRepository.save(
                new Account(UUID.randomUUID(), Money.eur("100.00"))
        );

        mockMvc.perform(patch("/api/accounts/{id}/block", account.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void shouldUnblockAccount() throws Exception {

        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        account.block();
        accountRepository.save(account);

        mockMvc.perform(patch("/api/accounts/{id}/unblock", account.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldRejectDepositOnBlockedAccount() throws Exception {

        Account account = new Account(
                UUID.randomUUID(),
                Money.eur("100.00")
        );

        account.block();
        accountRepository.save(account);

        mockMvc.perform(post("/api/accounts/{id}/deposits", account.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 20.00
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code")
                        .value("BUSINESS_RULE_VIOLATION"));
    }
}
