package by.nata.scheduler.job;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

@Log4j2
public class SimpleJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("SimpleJob says: " + jobKey + " executing at " + new Date());
    }
}
