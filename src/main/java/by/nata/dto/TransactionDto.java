package by.nata.dto;

import java.math.BigDecimal;
import java.sql.Date;

public record TransactionDto(Long id, BigDecimal amount, Date date,
                             String sAccount, String sBank,
                             String bAccount, String  bBank) {
}
