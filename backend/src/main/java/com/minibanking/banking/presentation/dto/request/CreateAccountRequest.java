package com.minibanking.banking.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(@NotNull(message = "Initial balance is required")
                                   @DecimalMin(value = "0.00", message = "Initial balance cannot be negative")
                                   @Digits(integer = 17, fraction = 2,
                                           message = "Balance must have at most 2 decimal places")
                                   BigDecimal initialBalance) {
}
