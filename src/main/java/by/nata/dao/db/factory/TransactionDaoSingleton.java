package by.nata.dao.db.factory;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.db.TransactionDaoImpl;
import by.nata.dao.db.connection.factory.DataSourceSingleton;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

@Log4j2
public class TransactionDaoSingleton {

    private static volatile TransactionDaoImpl instance;

    private TransactionDaoSingleton() {
        throw new IllegalStateException("Utility class");
    }

    public static ITransactionDao getInstance() throws PropertyVetoException, FileNotFoundException {
        log.info("Create TransactionDaoImpl instance...");
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
