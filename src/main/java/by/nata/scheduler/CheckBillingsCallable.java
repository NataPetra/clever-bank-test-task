package by.nata.scheduler;

import by.nata.dto.CheckBillingsDto;
import by.nata.service.api.IAccountService;
import by.nata.service.fabrics.AccountServiceSingleton;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.Callable;

@Log4j2
public class CheckBillingsCallable implements Callable<Void> {

    IAccountService instanceService = AccountServiceSingleton.getInstance();

    @Override
    public Void call() {
        List<CheckBillingsDto> checkBillingsDtos = instanceService.checkingAccounts();
        for (CheckBillingsDto checkBilling : checkBillingsDtos) {
            if (Boolean.TRUE.equals(checkBilling.isNeedPayInterest())) {
                instanceService.payInterest(checkBilling.account());
            }
        }
        return null;
    }
}
