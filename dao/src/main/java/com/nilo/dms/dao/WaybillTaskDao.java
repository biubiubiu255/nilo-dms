package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.WaybillDeliveryDeatilDO;
import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface WaybillTaskDao{

    List<WaybillTaskDo> queryNeedPayOrderByRider(String userId);

    List<WaybillDeliveryDeatilDO>queryDeliveryDetailInbound();
}
