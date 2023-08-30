package by.nata.dto;

import java.math.BigDecimal;

public record AccountDto(Long id, String accountNumber, BigDecimal amount) {
}
