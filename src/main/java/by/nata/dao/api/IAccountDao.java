package by.nata.dao.api;

import by.nata.dto.AccountDto;

public interface IAccountDao {

    AccountDto get (String accountNumber);
    void updateAmount(Long id, AccountDto accountDto);

}
