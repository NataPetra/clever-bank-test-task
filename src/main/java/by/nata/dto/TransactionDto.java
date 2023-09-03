package by.nata.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Date;

@Builder(setterPrefix = "with")
public record TransactionDto(Long id, BigDecimal amount, Date date,
                             String sAccount, String sBank,
                             String bAccount, String bBank) {
}
