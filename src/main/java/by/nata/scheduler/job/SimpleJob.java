package by.nata.scheduler.job;

import by.nata.dto.CheckBillingsDto;
import by.nata.scheduler.CheckBillingsCallable;
import by.nata.service.api.IAccountService;
import by.nata.service.fabrics.AccountServiceSingleton;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j2
public class SimpleJob implements Job {

    IAccountService instanceService = AccountServiceSingleton.getInstance();

    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Void> checkBillingsCallable = new CheckBillingsCallable();
        executorService.submit(checkBillingsCallable);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("SimpleJob says: " + jobKey + " executing at " + new Date());
    }
}
