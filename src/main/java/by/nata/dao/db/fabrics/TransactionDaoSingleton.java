package by.nata.dao.db.fabrics;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.db.TransactionDaoImpl;
import by.nata.dao.db.connection.fabrics.DataSourceSingleton;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

public class TransactionDaoSingleton {

    private volatile static TransactionDaoImpl instance;

    public static ITransactionDao getInstance() throws PropertyVetoException, FileNotFoundException {
        if (instance == null) {
            synchronized (TransactionDaoSingleton.class) {
                if (instance == null) {
                    instance = new TransactionDaoImpl(DataSourceSingleton.getInstance());
                }
            }
        }
        return instance;
    }
}
