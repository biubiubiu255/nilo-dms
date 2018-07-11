package com.nilo.dms.service.system.schedule;

import com.nilo.dms.common.utils.SendEmailUtil;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.HandleSmallDao;
import com.nilo.dms.dao.WaybillTaskDao;
import com.nilo.dms.dao.dataobject.WaybillDeliveryDeatilDO;
import com.nilo.dms.service.system.DeliveryScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * User: Alvin
 * Date: 2018/06/07 11:26
 * Just go for it and give it a try
 */
public class ScheduleMsn {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);

    @Autowired
    private HandleSmallDao handleSmallDao;

    public void run(){

        List<WaybillDeliveryDeatilDO> waybillDeliveryDeatilDOS = handleSmallDao.queryDeliveryDetailInbound();

        if (!waybillDeliveryDeatilDOS.isEmpty()){

            logger.warn("scan delivery detail sussess, count：" + waybillDeliveryDeatilDOS.size());

            handleSmallDao.insertAll(waybillDeliveryDeatilDOS);
        }else {
            logger.warn("scan delivery detail normal, count：" + waybillDeliveryDeatilDOS.size());
        }

    }
}
