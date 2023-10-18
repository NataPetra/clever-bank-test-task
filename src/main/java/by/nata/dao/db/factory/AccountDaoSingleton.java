package by.nata.dao.db.factory;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.AccountDaoImpl;
import by.nata.dao.db.connection.factory.DataSourceSingleton;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

@Log4j2
public class AccountDaoSingleton {

    private static volatile AccountDaoImpl instance;

    private AccountDaoSingleton() {
        throw new IllegalStateException("Utility class");
    }

    public static IAccountDao getInstance() throws PropertyVetoException, FileNotFoundException {
        log.info("Create AccountDaoImpl instance...");
        if (instance == null) {
            synchronized (AccountDaoSingleton.class) {
                if (instance == null) {
                    instance = new AccountDaoImpl(DataSourceSingleton.getInstance());
                }
            }
        }
        return instance;
    }
}
