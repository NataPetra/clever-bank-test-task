package by.nata.service;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.TransactionDto;
import by.nata.service.util.BankCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private ITransactionDao dao;

    @Mock
    private BankCheck bankCheck;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveTransaction() throws IOException {
        BigDecimal amount = new BigDecimal("100.00");
        Long sId = 1L;
        Long bId = 2L;
        String sAccount = "123";
        String sBank = "A";
        String bAccount = "321";
        String bBank = "B";
        String type = "REFILL";

        TransactionDto transactionDto = TransactionDto.builder()
                .withId(1L)
                .withAmount(amount)
                .withDate(Date.valueOf(LocalDate.now()))
                .withSAccount(sAccount)
                .withSBank(sBank)
                .withBBank(bBank)
                .withBAccount(bAccount)
                .build();

        doNothing().when(dao).create(amount, sId, bId, type);
        when(dao.getLast()).thenReturn(transactionDto);

        transactionService.saveTransaction(amount, sId, bId, type);

        verify(dao, times(1)).create(amount, sId, bId, type);

        verify(dao, times(1)).getLast();

        verify(bankCheck, times(1)).saveCheck(TransactionEnum.valueOf(type), transactionDto);
    }

}