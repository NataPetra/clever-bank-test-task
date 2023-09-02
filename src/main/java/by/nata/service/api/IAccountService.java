package by.nata.service.api;

import by.nata.dto.AccountDto;
import by.nata.dto.CheckBillingsDto;
import by.nata.dto.TransactionDto;

import java.util.List;

public interface IAccountService {

    AccountDto refill(String account, Double sum);

    AccountDto withdrawal(String account, Double sum);

    boolean isNeedPayInterest(String account);

    void payInterest(String account);

    List<CheckBillingsDto> checkingAccounts();

    void transferWithinOneBank(String sAccount, String bAccount, Double sum);

    void transferWithinDifferentBanks(String sAccount, String bAccount, Double sum);

}
