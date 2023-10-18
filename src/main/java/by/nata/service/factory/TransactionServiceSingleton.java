package by.nata.service.factory;

import by.nata.dao.provider.DaoProvider;
import by.nata.service.TransactionService;
import by.nata.service.api.ITransactionService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TransactionServiceSingleton {

    private static volatile TransactionService instance;

    private TransactionServiceSingleton() {
        throw new IllegalStateException("Utility class");
    }

    public static ITransactionService getInstance() {
        log.info("Create TransactionService instance...");
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
