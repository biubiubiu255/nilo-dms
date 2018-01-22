package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.AbnormalOrderDO;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AbnormalOrderDao extends BaseDao<Long, AbnormalOrderDO> {

    List<AbnormalOrderDO> queryAbnormalListBy(Map map);

    Long queryCountBy(Map map);

    AbnormalOrderDO queryByReferenceNo(@Param("merchantId")Long merchantId, @Param("referenceNo") String referenceNo);

    AbnormalOrderDO queryByOrderNo(@Param("merchantId")Long merchantId,@Param("orderNo")String orderNo);

    AbnormalOrderDO queryByAbnormalNo(@Param("merchantId")Long merchantId,@Param("abnormalNo")String abnormalNo);

}
