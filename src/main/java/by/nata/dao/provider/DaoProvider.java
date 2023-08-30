package by.nata.dao.provider;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.fabrics.AccountDaoSingleton;
import by.nata.dao.provider.api.IDaoProvider;

import java.beans.PropertyVetoException;

public class DaoProvider implements IDaoProvider {
    @Override
    public IAccountDao accountDao() {
        try {
            return AccountDaoSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new IllegalStateException(e);
        }
    }
}
