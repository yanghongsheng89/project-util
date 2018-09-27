package com.yang.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.MutableTrigger;

public class QuartzThread extends Thread{
    @Override
    public void run() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail detailΩ = JobBuilder.newJob(JobTest.class).withIdentity("test","test").build();
            JobDetail detail = JobBuilder.newJob(JobTest.class).withIdentity("testA","testA").storeDurably(false).build();
//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("test","test").startNow().build();
            MutableTrigger trigger = CronScheduleBuilder.cronSchedule("3/5 * * * * ? *").build();
            trigger.setKey(new TriggerKey("test"));
            scheduler.scheduleJob(detailΩ,trigger);
            MutableTrigger build = CronScheduleBuilder.cronSchedule("1/5 * * * * ? *").build();
//            detail.getJobBuilder().ofType(CronTrigger.class);
            build.setKey(new TriggerKey("testA"));
            scheduler.start();
//            scheduler.addJob(detail,true,true);
            scheduler.scheduleJob(detail,build);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}