package by.nata.dao.api;

import by.nata.dto.AccountDto;
import by.nata.dto.CheckBillingsDto;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountDao {

    AccountDto get (String accountNumber);
    void updateAmount(AccountDto accountDto);
    boolean isAccountExists(String accountNumber);
    BigDecimal checkAccountBalance(String accountNumber);
    List<String> getAccounts();

}
