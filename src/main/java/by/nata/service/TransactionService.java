package by.nata.service;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.TransactionDto;
import by.nata.service.api.ITransactionService;
import by.nata.service.util.BankCheck;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionDao dao;

    @SneakyThrows
    @Override
    public void saveTransaction(BigDecimal amount, Long sId, Long bId, String type) {
        dao.create(amount, sId, bId, type);
        TransactionDto lastTransaction = dao.getLast();
        BankCheck.saveCheck(TransactionEnum.valueOf(type), lastTransaction);
    }
}
