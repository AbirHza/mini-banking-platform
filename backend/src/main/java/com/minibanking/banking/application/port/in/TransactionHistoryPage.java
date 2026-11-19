package com.minibanking.banking.application.port.in;

import com.minibanking.banking.domain.model.AccountTransaction;

import java.util.List;

public record TransactionHistoryPage(List<AccountTransaction> transactions,
                                     int page,
                                     int size,
                                     long totalElements,
                                     int totalPages) {
}
