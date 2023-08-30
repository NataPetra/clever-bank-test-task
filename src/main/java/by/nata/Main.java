package by.nata;

import by.nata.scheduler.job.SimpleJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main {

//    public static void main(String[] args) throws SchedulerException {
//
//        SchedulerFactory sf = new StdSchedulerFactory();
//        Scheduler sched = sf.getScheduler();
//
//        JobDetail job = newJob(SimpleJob.class)
//                .withIdentity("job1", "group1")
//                .build();
//
//        CronTrigger trigger = newTrigger()
//                .withIdentity("trigger1", "group1")
//                .withSchedule(cronSchedule("0/30 * * * * ?"))
//                .build();
//
//        sched.scheduleJob(job, trigger);
//
//        //sched.start();
//
//
//
//        System.out.println("Hello world!");
//    }
}