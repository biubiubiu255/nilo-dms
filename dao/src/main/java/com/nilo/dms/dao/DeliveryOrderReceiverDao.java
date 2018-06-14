package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliveryOrderReceiverDao extends BaseDao<Long,DeliveryOrderReceiverDO> {

    List<DeliveryOrderReceiverDO> queryByOrderNos(@Param("merchantId")Long merchantId, @Param("orderNos")List<String> orderNo);

    DeliveryOrderReceiverDO queryByOrderNo(@Param("merchantId")Long merchantId, @Param("orderNo")String orderNo);


}
