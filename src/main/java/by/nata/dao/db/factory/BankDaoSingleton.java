package by.nata.dao.db.factory;

import by.nata.dao.api.IBankDao;
import by.nata.dao.db.BankDaoImpl;
import by.nata.dao.db.connection.factory.DataSourceSingleton;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

@Log4j2
public class BankDaoSingleton {

    private static volatile BankDaoImpl instance;

    private BankDaoSingleton() {
        throw new IllegalStateException("Utility class");
    }

    public static IBankDao getInstance() throws PropertyVetoException, FileNotFoundException {
        log.info("Create BankDaoImpl instance...");
        if (instance == null) {
            synchronized (BankDaoSingleton.class) {
                if (instance == null) {
                    instance = new BankDaoImpl(DataSourceSingleton.getInstance());
                }
            }
        }
        return instance;
    }

}
