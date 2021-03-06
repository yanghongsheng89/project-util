package com.yang.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobTest implements Job {
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        String format = sf.format(date);
        System.err.println(format+context.getJobDetail().getKey());
    }
}