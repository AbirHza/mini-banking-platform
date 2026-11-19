package com.minibanking.banking.presentation.dto.response;

import com.minibanking.banking.application.port.in.TransactionHistoryPage;

import java.util.List;

public record TransactionHistoryResponse(
        List<AccountTransactionResponse> transactions,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static TransactionHistoryResponse from(
            TransactionHistoryPage result
    ) {
        return new TransactionHistoryResponse(
                result.transactions()
                        .stream()
                        .map(AccountTransactionResponse::from)
                        .toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
