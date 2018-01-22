package com.nilo.dms.service.system.impl;

import com.nilo.dms.common.enums.ScheduleStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.ScheduleDao;
import com.nilo.dms.dao.dataobject.ScheduleDO;
import com.nilo.dms.service.system.ScheduleService;
import com.nilo.dms.service.system.model.ScheduleJob;
import com.nilo.dms.service.system.schedule.ScheduleJobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/1.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    public List<ScheduleJob> queryBy(String merchantId, ScheduleStatusEnum statusEnum) {

        Long merchantIdL = null == merchantId ? null : Long.parseLong(merchantId);
        List<ScheduleDO> scheduleDOList = scheduleDao.findAll(merchantIdL, statusEnum == null ? null : statusEnum.getCode());

        List<ScheduleJob> list = new ArrayList<>();
        if (scheduleDOList == null || scheduleDOList.size() == 0) {
            return list;
        }
        for (ScheduleDO s : scheduleDOList) {
            list.add(convert(s));
        }

        return list;
    }

    @Override
    public void updateScheduleJob(ScheduleJob job) {

        ScheduleDO query = scheduleDao.queryByJobName(job.getJobName());
        if (query == null) {
            throw new IllegalArgumentException("JobName:" + job.getJobName() + " Not Exist");
        }
        ScheduleDO scheduleDO = convert(job);
        scheduleDao.update(scheduleDO);
        //更新定时任务
        if (!StringUtil.equals(query.getCornExpression(), job.getCornExpression())) {
            try {
                ScheduleJobManager.modifyJobTime(job.getJobName(), job.getCornExpression());
            } catch (Exception e) {
                logger.error("Modify Job Failed.", e);
            }
        }
    }

    @Override
    public void addScheduleJob(ScheduleJob job) {
        ScheduleDO query = scheduleDao.queryByJobName(job.getJobName());
        if (query != null) {
            throw new IllegalArgumentException("JobName:" + job.getJobName() + " Exist");
        }
        ScheduleDO scheduleDO = convert(job);
        scheduleDao.insert(scheduleDO);
        try {
            Class cls = Class.forName(job.getClassName());
            ScheduleJobManager.addJob(job.getJobName(), cls, job.getCornExpression());
        } catch (Exception e) {
            logger.error("Modify Job Failed.", e);
        }
    }

    private ScheduleJob convert(ScheduleDO scheduleDO) {

        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setMerchantId("" + scheduleDO.getMerchantId());
        scheduleJob.setStatus(ScheduleStatusEnum.getEnum(scheduleDO.getStatus()));
        scheduleJob.setClassName(scheduleDO.getClassName());
        scheduleJob.setCornExpression(scheduleDO.getCornExpression());
        scheduleJob.setJobName(scheduleDO.getJobName());
        scheduleJob.setVersion(scheduleDO.getVersion());
        scheduleJob.setRemark(scheduleDO.getRemark());
        scheduleJob.setCreatedTime(scheduleDO.getCreatedTime());
        scheduleJob.setUpdatedTime(scheduleDO.getUpdatedTime());

        return scheduleJob;
    }

    private ScheduleDO convert(ScheduleJob job) {

        ScheduleDO scheduleDO = new ScheduleDO();
        if (job.getStatus() != null) {
            scheduleDO.setStatus(job.getStatus().getCode());
        }
        scheduleDO.setMerchantId(Long.parseLong(job.getMerchantId()));
        scheduleDO.setClassName(job.getClassName());
        scheduleDO.setCornExpression(job.getCornExpression());
        scheduleDO.setJobName(job.getJobName());
        scheduleDO.setVersion(job.getVersion());
        scheduleDO.setRemark(job.getRemark());
        scheduleDO.setCreatedTime(job.getCreatedTime());
        scheduleDO.setUpdatedTime(job.getUpdatedTime());

        return scheduleDO;
    }
}
