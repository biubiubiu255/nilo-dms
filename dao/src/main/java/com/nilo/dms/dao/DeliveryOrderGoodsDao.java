package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderGoodsDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliveryOrderGoodsDao extends BaseDao<Long,DeliveryOrderGoodsDO> {

    List<DeliveryOrderGoodsDO> queryByOrderNo(@Param("merchantId")Long merchantId, @Param("orderNo")String orderNo);

    List<DeliveryOrderGoodsDO> queryByOrderNos(@Param("merchantId")Long merchantId, @Param("orderNos")List<String> orderNo);

    void deleteBy(@Param("merchantId")Long merchantId,@Param("orderNo")String orderNo,@Param("goodsId")String goodsId);

}
