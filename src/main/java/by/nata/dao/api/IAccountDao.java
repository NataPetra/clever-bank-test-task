package by.nata.dao.api;

import by.nata.dao.entity.Account;

import java.math.BigDecimal;

public interface IAccountDao {

    Account get (String accountNumber);
    void update(int id, Account account);

}
