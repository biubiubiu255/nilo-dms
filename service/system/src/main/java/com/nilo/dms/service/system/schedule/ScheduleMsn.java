package com.nilo.dms.service.system.schedule;

import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.WaybillTaskDao;
import com.nilo.dms.dao.dataobject.WaybillDeliveryDeatilDO;
import com.nilo.dms.service.system.DeliveryScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * User: Alvin
 * Date: 2018/06/07 11:26
 * Just go for it and give it a try
 */
public class ScheduleMsn {

    @Autowired
    private WaybillTaskDao waybillTaskDao;

    public void run(){
        //List<WaybillDeliveryDeatilDO> waybillDeliveryDeatilDOS = waybillTaskDao.queryDeliveryDetailInbound();
        //System.out.println("本次测试 = 本次测试哈哈哈哈哈哈哈");
    }
}
