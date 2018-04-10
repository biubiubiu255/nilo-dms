package com.nilo.dms.service.system;

import com.nilo.dms.common.enums.ScheduleStatusEnum;
import com.nilo.dms.dto.system.ScheduleJob;

import java.util.List;

/**
 * Created by admin on 2017/12/1.
 */
public interface ScheduleService {

    List<ScheduleJob> queryBy(String merchantId, ScheduleStatusEnum statusEnum);

    void updateScheduleJob(ScheduleJob job);

    void addScheduleJob(ScheduleJob job);

}
