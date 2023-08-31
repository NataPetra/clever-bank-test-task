package by.nata.dao.api;

import by.nata.dto.AccountDto;

import java.math.BigDecimal;

public interface IAccountDao {

    AccountDto get (String accountNumber);
    void updateAmount(AccountDto accountDto);
    boolean isAccountExists(String accountNumber);
    BigDecimal checkAccountBalance(String accountNumber);

}
