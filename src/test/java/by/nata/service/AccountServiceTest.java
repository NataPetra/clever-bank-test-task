package by.nata.service;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private IAccountDao dao;

    @Mock
    private TransactionService transactionService;

    public static final String ACCOUNT = "Account-5185";
    public static final double SUM = 100.00;
    public static final String S_ACCOUNT = "1234567890";
    public static final String B_ACCOUNT = "2345678901";

    @Test
    void testRefill() {
        String accountNumber = ACCOUNT;
        double sum = SUM;

        AccountDto accountDto = new AccountDto(1L, accountNumber, new BigDecimal("500.00"));

        when(dao.get(accountNumber)).thenReturn(accountDto);
        doNothing().when(dao).updateAmount(any(AccountDto.class));
        doNothing().when(transactionService).saveTransaction(
                BigDecimal.valueOf(sum), accountDto.id(), accountDto.id(), TransactionEnum.REFILL.toString());
        when(dao.isAccountExists(accountNumber)).thenReturn(true);

        AccountDto updatedAccountDto = accountService.refill(accountNumber, sum);

        verify(dao, times(1)).get(accountNumber);
        verify(dao, times(1)).updateAmount(any(AccountDto.class));
        verify(transactionService, times(1)).saveTransaction(
                BigDecimal.valueOf(sum), accountDto.id(), accountDto.id(), TransactionEnum.REFILL.toString());

        assertEquals(accountNumber, updatedAccountDto.accountNumber());
        assertEquals(new BigDecimal("600.00"), updatedAccountDto.amount());
    }

    @Test
    void testWithdrawal() {
        String accountNumber = ACCOUNT;
        double sum = SUM;

        AccountDto accountDto = new AccountDto(1L, accountNumber, new BigDecimal("500.00"));

        when(dao.get(accountNumber)).thenReturn(accountDto);
        doNothing().when(dao).updateAmount(any(AccountDto.class));
        doNothing().when(transactionService).saveTransaction(
                BigDecimal.valueOf(sum), accountDto.id(), accountDto.id(), TransactionEnum.WITHDRAWAL.toString());
        when(dao.isAccountExists(accountNumber)).thenReturn(true);

        AccountDto updatedAccountDto = accountService.withdrawal(accountNumber, sum);

        verify(dao, times(1)).get(accountNumber);
        verify(dao, times(1)).updateAmount(any(AccountDto.class));
        verify(transactionService, times(1)).saveTransaction(
                BigDecimal.valueOf(sum), accountDto.id(), accountDto.id(), TransactionEnum.WITHDRAWAL.toString());

        assertEquals(accountNumber, updatedAccountDto.accountNumber());
        assertEquals(new BigDecimal("400.00"), updatedAccountDto.amount());
    }

    @Test
    void testIsNeedPayInterest() {
        String accountNumber = ACCOUNT;

        when(dao.checkAccountBalance(accountNumber)).thenReturn(new BigDecimal("1000.00"));
        LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        LocalDate currentDate = LocalDate.now();

        boolean needPayInterest = accountService.isNeedPayInterest(accountNumber);

        verify(dao, times(1)).checkAccountBalance(accountNumber);
        assertEquals(lastDayOfMonth.equals(currentDate) && new BigDecimal("1000.00").compareTo(BigDecimal.ZERO) > 0, needPayInterest);
    }

    @Test
    void testTransferWithinOneBank() {
        String sAccount = S_ACCOUNT;
        String bAccount = B_ACCOUNT;
        double sum = SUM;

        when(dao.isAccountExists(sAccount)).thenReturn(true);
        when(dao.isAccountExists(bAccount)).thenReturn(true);

        AccountDto withdrawal = new AccountDto(1L, sAccount, new BigDecimal("500.00"));
        AccountDto refill = new AccountDto(2L, bAccount, new BigDecimal("300.00"));

        when(dao.get(sAccount)).thenReturn(withdrawal);
        when(dao.get(bAccount)).thenReturn(refill);
        doNothing().when(dao).updateAmount(any(AccountDto.class));
        doNothing().when(transactionService).saveTransaction(
                BigDecimal.valueOf(sum), withdrawal.id(), refill.id(), TransactionEnum.TRANSFER.toString());

        accountService.transferWithinOneBank(sAccount, bAccount, sum);

        verify(dao, times(1)).isAccountExists(sAccount);
        verify(dao, times(1)).isAccountExists(bAccount);
        verify(dao, times(1)).get(sAccount);
        verify(dao, times(1)).get(bAccount);
        verify(dao, times(2)).updateAmount(any(AccountDto.class));
        verify(transactionService, times(1)).saveTransaction(
                BigDecimal.valueOf(sum), withdrawal.id(), refill.id(), TransactionEnum.TRANSFER.toString());
    }

    @Test
    void testTransferWithinDifferentBanks() {
        String sAccount = S_ACCOUNT;
        String bAccount = B_ACCOUNT;
        double sum = SUM;

        AccountDto senderAccount = new AccountDto(1L, sAccount, new BigDecimal("500.00"));
        AccountDto beneficiaryAccount = new AccountDto(2L, bAccount, new BigDecimal("300.00"));

        when(dao.transferWithinDifferentBanks(sAccount, bAccount, BigDecimal.valueOf(sum)))
                .thenReturn(Arrays.asList(senderAccount, beneficiaryAccount));
        doNothing().when(transactionService).saveTransaction(
                BigDecimal.valueOf(sum), senderAccount.id(), beneficiaryAccount.id(), TransactionEnum.TRANSFER.toString());

        accountService.transferWithinDifferentBanks(sAccount, bAccount, sum);

        verify(dao, times(1)).transferWithinDifferentBanks(sAccount, bAccount, BigDecimal.valueOf(sum));
        verify(transactionService, times(1)).saveTransaction(
                BigDecimal.valueOf(sum), senderAccount.id(), beneficiaryAccount.id(), TransactionEnum.TRANSFER.toString());
    }
}