package com.nilo.dms.service.system.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class NotifyScheduler implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final AtomicInteger processing = new AtomicInteger(
            0);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if (processing.compareAndSet(0, 1)) {
            try {
            } catch (Exception ex) {
                logger.error("queryRetryList failed." + ex.getMessage());
            } finally {
                processing.set(0);
            }
        } else {
        }
    }


}
