package by.nata.dao.provider.api;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.api.ITransactionDao;

public interface IDaoProvider {

    IAccountDao accountDao();

    ITransactionDao transactionDao();
}
