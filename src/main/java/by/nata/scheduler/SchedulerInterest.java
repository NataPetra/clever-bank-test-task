package by.nata.scheduler;

import by.nata.scheduler.job.InterestCalculationCheckJob;
import lombok.SneakyThrows;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerInterest {

    @SneakyThrows
    public static Scheduler getScheduler(){
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = newJob(InterestCalculationCheckJob.class)
                .withIdentity("job", "group")
                .build();

        CronTrigger trigger = newTrigger()
                .withIdentity("trigger", "group")
                .withSchedule(cronSchedule("0/30 * * * * ?"))
                .build();

        sched.scheduleJob(job, trigger);
        return sched;
    }
}
