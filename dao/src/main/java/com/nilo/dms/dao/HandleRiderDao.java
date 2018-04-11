package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleRiderDao extends BaseDao<Long, RiderDeliveryDO> {

    List<RiderDeliveryDO> queryRiderDeliveryBig(@Param("ob") RiderDeliveryDO riderDeliveryDO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer queryRiderDeliveryBigCount(@Param("ob") RiderDeliveryDO riderDeliveryDO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<RiderDeliverySmallDO> queryDeliverySmall(RiderDeliverySmallDO riderDeliverySmallDO);

    void insertBig(RiderDeliveryDO riderDeliveryDO);

    void insertSmall(RiderDeliverySmallDO riderDeliverySmallDO);

    void insertSmalls(@Param("emps") List<RiderDeliverySmallDO> list);

    void upBigBy(RiderDeliveryDO riderDeliveryDO);

    void upBigStatus(@Param("handleNo") String handleNo, @Param("status") Integer status);

    void deleteSmallByHandleNo(RiderDeliverySmallDO riderDeliverySmallDO);
}
