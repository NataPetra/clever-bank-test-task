package by.nata.dao.provider;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.api.ITransactionDao;
import by.nata.dao.db.factory.AccountDaoSingleton;
import by.nata.dao.db.factory.TransactionDaoSingleton;
import by.nata.dao.provider.api.IDaoProvider;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

public class DaoProvider implements IDaoProvider {

    private static volatile DaoProvider instance;

    @Override
    public IAccountDao accountDao() {
        try {
            return AccountDaoSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new IllegalStateException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ITransactionDao transactionDao() {
        try {
            return TransactionDaoSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new IllegalStateException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static IDaoProvider getInstance() {
        if (instance == null) {
            synchronized (DaoProvider.class) {
                if (instance == null) {
                    instance = new DaoProvider();
                }
            }
        }
        return instance;
    }
}
