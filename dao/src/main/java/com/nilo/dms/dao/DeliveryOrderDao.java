package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliveryOrderDao extends BaseDao<Long,DeliveryOrderDO> {

    List<DeliveryOrderDO> queryDeliveryOrderListBy(Map map);

    Long queryCountBy(Map map);

    DeliveryOrderDO queryByReferenceNo(@Param("merchantId")Long merchantId,@Param("referenceNo") String referenceNo);

    DeliveryOrderDO queryByOrderNo(@Param("merchantId")Long merchantId,@Param("orderNo")String orderNo);

    List<DeliveryOrderDO> queryByOrderNos(@Param("merchantId")Long merchantId,@Param("orderNos")List<String> orderNos);

    List<DeliveryOrderDO> queryByPackageNo(@Param("merchantId")Long merchantId,@Param("packageNo") String packageNo);

}
