package by.nata.service;

import by.nata.config.Config;
import by.nata.config.ConfigHandler;
import by.nata.dao.api.IAccountDao;
import by.nata.dto.AccountDto;
import by.nata.dto.CheckBillingsDto;
import by.nata.exception.InsufficientFundsException;
import by.nata.service.api.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountDao dao;

    private static final String MESSAGE_FOR_ABSENT_ACCOUNT = "This account number does not exist";

    @Override
    public AccountDto refill(String account, Double sum) {
        checkArgs(account, sum);
        AccountDto accountDto = dao.get(account);
        AccountDto updateAccountAdo;
        if (accountDto!=null){
            BigDecimal updatedAmount = accountDto.amount().add(BigDecimal.valueOf(sum));
            updateAccountAdo = new AccountDto(accountDto.id(), accountDto.accountNumber(), updatedAmount);
            dao.updateAmount(updateAccountAdo);
        } else {
            throw new RuntimeException("Something went wrong");
        }
        return updateAccountAdo;
    }

    @Override
    public AccountDto withdrawal(String account, Double sum) {
        checkArgs(account, sum);
        AccountDto accountDto = dao.get(account);
        AccountDto updateAccountAdo;
        if (accountDto!=null){
            if (accountDto.amount().compareTo(BigDecimal.valueOf(sum)) < 0){
                throw new InsufficientFundsException("You do not have enough funds to write off");
            }
            BigDecimal updatedAmount = accountDto.amount().subtract(BigDecimal.valueOf(sum));
            updateAccountAdo = new AccountDto(accountDto.id(), accountDto.accountNumber(), updatedAmount);
            dao.updateAmount(updateAccountAdo);
        } else {
            throw new RuntimeException("Something went wrong");
        }
        return updateAccountAdo;
    }

    @Override
    public boolean isNeedPayInterest(String account) {
        if (!dao.isAccountExists(account)){
            throw new IllegalArgumentException(MESSAGE_FOR_ABSENT_ACCOUNT);
        }
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
        BigDecimal  multiply= divide.multiply(BigDecimal.valueOf(interest));
        Double result = balance.add(multiply).doubleValue();
        withdrawal(account, result);
    }

    @Override
    public List<CheckBillingsDto> checkingAccounts() {
        List<CheckBillingsDto> checkBillingsDtos = new ArrayList<>();
        List<String> accounts = dao.getAccounts();
        for (String account: accounts) {
            if(isNeedPayInterest(account)){
                checkBillingsDtos.add(new CheckBillingsDto(true,account));
            }
        }
        return checkBillingsDtos;
    }

    private void checkArgs(String account, Double sum) {
        if (isNull(account) || sum <= 0) {
            throw new IllegalArgumentException("You did not enter an account number or an incorrect amount");
        }
        if (!dao.isAccountExists(account)){
            throw new IllegalArgumentException(MESSAGE_FOR_ABSENT_ACCOUNT);
        }
    }

}
