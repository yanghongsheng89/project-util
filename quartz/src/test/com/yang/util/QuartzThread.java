package com.yang.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.MutableTrigger;

public class QuartzThread extends Thread{
    @Override
    public void run() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail detailΩ = JobBuilder.newJob(JobTest.class).withIdentity("util","util").build();
            JobDetail detail = JobBuilder.newJob(JobTest.class).withIdentity("testA","testA").storeDurably(false).build();
//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("util","util").startNow().build();
            MutableTrigger trigger = CronScheduleBuilder.cronSchedule("3/5 * * * * ? *").build();
            trigger.setKey(new TriggerKey("util"));
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