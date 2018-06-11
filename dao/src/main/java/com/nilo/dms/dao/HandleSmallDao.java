package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RiderDelivery;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dao.dataobject.WaybillDeliveryDeatilDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleSmallDao extends BaseDao<Long, WaybillDeliveryDeatilDO> {

    List<WaybillDeliveryDeatilDO>queryDeliveryDetailInbound();

    int insertAll(List<WaybillDeliveryDeatilDO> list);

}
