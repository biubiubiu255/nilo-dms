package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderSenderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliveryOrderSenderDao extends BaseDao<Long,DeliveryOrderSenderDO> {

    List<DeliveryOrderSenderDO> queryByOrderNos(@Param("merchantId")Long merchantId, @Param("orderNos")List<String> orderNo);

    DeliveryOrderSenderDO queryByOrderNo(@Param("merchantId")Long merchantId, @Param("orderNo")String orderNo);

//    DeliveryOrderSenderDO queryByOrderNo
}
