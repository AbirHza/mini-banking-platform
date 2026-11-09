package com.minibanking.banking;

import com.minibanking.banking.application.port.out.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BankingBackendApplicationTests {

    @MockitoBean
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
    }

}
