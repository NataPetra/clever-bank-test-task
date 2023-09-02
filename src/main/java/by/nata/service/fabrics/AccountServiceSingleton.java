package by.nata.service.fabrics;

import by.nata.dao.provider.DaoProvider;
import by.nata.service.AccountService;
import by.nata.service.api.IAccountService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class AccountServiceSingleton {

    private volatile static AccountService instance;

    public static IAccountService getInstance() {
        log.info("Create AccountService instance...");
        if (instance == null) {
            synchronized (AccountServiceSingleton.class) {
                if (instance == null) {
                    instance = new AccountService(DaoProvider.getInstance().accountDao(), TransactionServiceSingleton.getInstance());
                }
            }
        }
        return instance;
    }

}
