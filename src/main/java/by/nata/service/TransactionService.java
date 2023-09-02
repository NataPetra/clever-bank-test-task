package by.nata.service;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.TransactionDto;
import by.nata.service.api.ITransactionService;
import by.nata.service.util.BankCheck;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Log4j2
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionDao dao;

    @SneakyThrows
    @Override
    public void saveTransaction(BigDecimal amount, Long sId, Long bId, String type) {
        log.info("Call saveTransaction() method from service");
        dao.create(amount, sId, bId, type);
        TransactionDto lastTransaction = dao.getLast();
        BankCheck.saveCheck(TransactionEnum.valueOf(type), lastTransaction);
    }
}
