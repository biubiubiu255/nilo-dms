package com.nilo.dms.service.system.schedule;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.system.SpringContext;
import com.nilo.dms.service.system.model.NotifyResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        }else{
        }
    }


}
