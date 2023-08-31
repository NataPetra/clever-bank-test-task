package by.nata.service.api;

import by.nata.dto.AccountDto;

public interface IAccountService {

    AccountDto refill(String account, Double sum);
    AccountDto withdrawal(String account, Double sum);
    boolean isNeedPayInterest(String account);
    void payInterest(String account);
}
