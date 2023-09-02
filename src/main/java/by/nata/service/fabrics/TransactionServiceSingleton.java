package by.nata.service.fabrics;

import by.nata.dao.provider.DaoProvider;
import by.nata.service.TransactionService;
import by.nata.service.api.ITransactionService;

public class TransactionServiceSingleton {

    private volatile static TransactionService instance;

    public static ITransactionService getInstance() {
        if (instance == null) {
            synchronized (TransactionServiceSingleton.class) {
                if (instance == null) {
                    instance = new TransactionService(DaoProvider.getInstance().transactionDao());
                }
            }
        }
        return instance;
    }
}
