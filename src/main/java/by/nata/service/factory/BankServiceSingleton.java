package by.nata.service.factory;

import by.nata.dao.provider.DaoProvider;
import by.nata.service.BankService;
import by.nata.service.api.IBankService;
import by.nata.service.mapper.BankMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BankServiceSingleton {

    private static volatile BankService instance;

    private BankServiceSingleton() {
        throw new IllegalStateException("Utility class");
    }

    public static IBankService getInstance() {
        log.info("Create BankService instance...");
        if (instance == null) {
            synchronized (AccountServiceSingleton.class) {
                if (instance == null) {
                    instance = new BankService(DaoProvider.getInstance().bankDao(), BankMapper.INSTANCE);
                }
            }
        }
        return instance;
    }
}
