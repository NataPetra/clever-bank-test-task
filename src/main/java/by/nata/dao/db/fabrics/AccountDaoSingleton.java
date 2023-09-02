package by.nata.dao.db.fabrics;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.AccountDaoImpl;
import by.nata.dao.db.connection.fabrics.DataSourceSingleton;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;

@Log4j2
@NoArgsConstructor
public class AccountDaoSingleton {

    private volatile static AccountDaoImpl instance;

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
