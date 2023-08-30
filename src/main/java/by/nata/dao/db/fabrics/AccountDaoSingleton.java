package by.nata.dao.db.fabrics;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.AccountDaoImpl;
import by.nata.dao.db.connection.fabrics.DataSourceSingleton;
import lombok.NoArgsConstructor;

import java.beans.PropertyVetoException;

@NoArgsConstructor
public class AccountDaoSingleton {

    private volatile static AccountDaoImpl instance;

    public static IAccountDao getInstance() throws PropertyVetoException {
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
