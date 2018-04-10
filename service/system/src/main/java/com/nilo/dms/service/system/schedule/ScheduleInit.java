package com.nilo.dms.service.system.schedule;

import com.nilo.dms.common.enums.ScheduleStatusEnum;
import com.nilo.dms.dao.ScheduleDao;
import com.nilo.dms.dao.dataobject.ScheduleDO;
import com.nilo.dms.dto.system.ScheduleJob;
import com.nilo.dms.service.system.ScheduleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/11/30.
 */
@Component
public class ScheduleInit implements InitializingBean {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {

        //查询所有定时任务列表
        List<ScheduleJob> list = scheduleService.queryBy(null, ScheduleStatusEnum.NORMAL);
        if (list == null || list.size() == 0) {
            return;
        }
        //添加任务
        for (ScheduleJob scheduleJob : list) {
            Class cls = Class.forName(scheduleJob.getClassName());
            ScheduleJobManager.addJob(scheduleJob.getMerchantId() + "_" + scheduleJob.getJobName(), cls, scheduleJob.getCornExpression());
        }
    }
}
