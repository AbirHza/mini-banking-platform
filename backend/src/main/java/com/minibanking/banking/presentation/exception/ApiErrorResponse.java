package com.minibanking.banking.presentation.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(String code,
                               String message,
                               Map<String, String> details,
                               LocalDateTime timestamp) {
}
