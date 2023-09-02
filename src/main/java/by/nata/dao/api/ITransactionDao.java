package by.nata.dao.api;

import by.nata.dto.TransactionDto;

import java.math.BigDecimal;
import java.sql.Date;


public interface ITransactionDao {

    void create(BigDecimal amount, Long sId, Long bId, String type);

    TransactionDto getLast();
}
