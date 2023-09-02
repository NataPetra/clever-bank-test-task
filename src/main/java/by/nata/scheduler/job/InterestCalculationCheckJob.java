package by.nata.scheduler.job;

import by.nata.scheduler.CheckBillingsCallable;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InterestCalculationCheckJob implements Job {

    @SneakyThrows
    public void execute(JobExecutionContext context) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Void> checkBillingsCallable = new CheckBillingsCallable();
        executorService.submit(checkBillingsCallable);
    }
}
