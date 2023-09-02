package by.nata.service.api;

import by.nata.dto.TransactionDto;

import java.math.BigDecimal;

public interface ITransactionService {

    void saveTransaction(BigDecimal amount, Long sId, Long bId, String type);

}
