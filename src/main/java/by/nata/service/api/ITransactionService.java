package by.nata.service.api;

import java.math.BigDecimal;

public interface ITransactionService {

    void saveTransaction(BigDecimal amount, Long sId, Long bId, String type);

}
