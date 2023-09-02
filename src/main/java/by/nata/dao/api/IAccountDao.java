package by.nata.dao.api;

import by.nata.dto.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountDao {

    AccountDto get(String accountNumber);

    void updateAmount(AccountDto accountDto);

    boolean isAccountExists(String accountNumber);

    BigDecimal checkAccountBalance(String accountNumber);

    List<String> getAccounts();

    List<AccountDto> transferWithinDifferentBanks(String sAccount, String bAccount, BigDecimal amount);
}
