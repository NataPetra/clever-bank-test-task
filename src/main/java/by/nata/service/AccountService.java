package by.nata.service;

import by.nata.config.Config;
import by.nata.config.ConfigHandler;
import by.nata.dao.api.IAccountDao;
import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.AccountDto;
import by.nata.dto.CheckBillingsDto;
import by.nata.exception.InsufficientFundsException;
import by.nata.service.api.IAccountService;
import by.nata.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountDao dao;

    private final ITransactionService transactionService;

    public static final String WRONG = "Something went wrong";
    public static final String EMPTY_ACCOUNT_OR_AN_INCORRECT_AMOUNT = "You did not enter an account number or an incorrect amount";
    private static final String MESSAGE_FOR_ABSENT_ACCOUNT = "This account number does not exist";

    @Override
    public AccountDto refill(String account, Double sum) {
        log.info("Call refill() method from service");
        checkArgs(account, sum);
        AccountDto accountDto = dao.get(account);
        AccountDto updateAccountAdo;
        if (accountDto != null) {
            BigDecimal updatedAmount = accountDto.amount().add(BigDecimal.valueOf(sum));
            log.info("UserAmount is " + updatedAmount);
            updateAccountAdo = new AccountDto(accountDto.id(), accountDto.accountNumber(), updatedAmount);
            dao.updateAmount(updateAccountAdo);
            transactionService.saveTransaction(
                    BigDecimal.valueOf(sum), updateAccountAdo.id(),
                    updateAccountAdo.id(), TransactionEnum.REFILL.toString()
            );
        } else {
            throw new RuntimeException(WRONG);
        }
        return updateAccountAdo;
    }

    @Override
    public AccountDto withdrawal(String account, Double sum) {
        log.info("Call withdrawal() method from service");
        checkArgs(account, sum);
        AccountDto accountDto = dao.get(account);
        AccountDto updateAccountDto;
        if (accountDto != null) {
            if (accountDto.amount().compareTo(BigDecimal.valueOf(sum)) < 0) {
                throw new InsufficientFundsException("You do not have enough funds to write off");
            }
            BigDecimal updatedAmount = accountDto.amount().subtract(BigDecimal.valueOf(sum));
            updateAccountDto = new AccountDto(accountDto.id(), accountDto.accountNumber(), updatedAmount);
            dao.updateAmount(updateAccountDto);
            transactionService.saveTransaction(
                    BigDecimal.valueOf(sum), updateAccountDto.id(),
                    updateAccountDto.id(), TransactionEnum.WITHDRAWAL.toString()
            );
        } else {
            throw new RuntimeException(WRONG);
        }
        return updateAccountDto;
    }

    @Override
    public boolean isNeedPayInterest(String account) {
        BigDecimal balance = dao.checkAccountBalance(account);
        Temporal lastDayOfMonth = TemporalAdjusters.lastDayOfMonth().adjustInto(LocalDate.now());
        LocalDate now = LocalDate.now();
        return balance.compareTo(BigDecimal.ZERO) > 0 && now.isEqual(ChronoLocalDate.from(lastDayOfMonth));
    }

    @SneakyThrows
    @Override
    public void payInterest(String account) {
        ConfigHandler handler = ConfigHandler.getInstance();
        Config config = handler.getConfig();
        Integer interest = config.getInterestToBeCharged();
        BigDecimal balance = dao.checkAccountBalance(account);
        BigDecimal divide = balance.divide(BigDecimal.valueOf(100));
        BigDecimal multiply = divide.multiply(BigDecimal.valueOf(interest));
        Double result = balance.add(multiply).doubleValue();
        withdrawal(account, result);
    }

    @Override
    public List<CheckBillingsDto> checkingAccounts() {
        List<CheckBillingsDto> checkBillingsDtos = new ArrayList<>();
        List<String> accounts = dao.getAccounts();
        for (String account : accounts) {
            if (isNeedPayInterest(account)) {
                checkBillingsDtos.add(new CheckBillingsDto(true, account));
            }
        }
        return checkBillingsDtos;
    }

    @Override
    public void transferWithinOneBank(String sAccount, String bAccount, Double sum) {
        log.info("Call transferWithinOneBank() method from service");
        if (isNull(sAccount) || isNull(bAccount) || sum <= 0) {
            throw new IllegalArgumentException(EMPTY_ACCOUNT_OR_AN_INCORRECT_AMOUNT);
        }
        if (!dao.isAccountExists(sAccount) || !dao.isAccountExists(bAccount)) {
            throw new IllegalArgumentException(MESSAGE_FOR_ABSENT_ACCOUNT);
        }
        AccountDto withdrawal = dao.get(sAccount);
        AccountDto refill = dao.get(bAccount);
        if (withdrawal != null && refill != null) {
            if (withdrawal.amount().compareTo(BigDecimal.valueOf(sum)) < 0) {
                throw new InsufficientFundsException("You do not have enough funds to write off");
            }
            BigDecimal updatedSAmount = withdrawal.amount().subtract(BigDecimal.valueOf(sum));
            dao.updateAmount(new AccountDto(withdrawal.id(), withdrawal.accountNumber(), updatedSAmount));
            BigDecimal updatedBAmount = refill.amount().add(BigDecimal.valueOf(sum));
            dao.updateAmount(new AccountDto(refill.id(), refill.accountNumber(), updatedBAmount));
        } else {
            throw new RuntimeException(WRONG);
        }
        transactionService.saveTransaction(
                BigDecimal.valueOf(sum), withdrawal.id(),
                refill.id(), TransactionEnum.TRANSFER.toString());
    }

    @Override
    public synchronized void transferWithinDifferentBanks(String sAccount, String bAccount, Double sum) {
        log.info("Call transferWithinDifferentBanks() method from service");
        List<AccountDto> accountDtos = dao.transferWithinDifferentBanks(sAccount, bAccount, BigDecimal.valueOf(sum));
        transactionService.saveTransaction(
                BigDecimal.valueOf(sum), accountDtos.get(0).id(),
                accountDtos.get(1).id(), TransactionEnum.TRANSFER.toString());
    }


    private void checkArgs(String account, Double sum) {
        if (isNull(account) || sum <= 0) {
            throw new IllegalArgumentException(EMPTY_ACCOUNT_OR_AN_INCORRECT_AMOUNT);
        }
        if (!dao.isAccountExists(account)) {
            throw new IllegalArgumentException(MESSAGE_FOR_ABSENT_ACCOUNT);
        }
    }

}
