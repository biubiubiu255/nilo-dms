package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RiderDelivery;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleRiderDao extends BaseDao<Long, RiderDelivery> {

    List<RiderDelivery> queryRiderDeliveryBig(@Param("ob") RiderDelivery riderDelivery, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer queryRiderDeliveryBigCount(@Param("ob") RiderDelivery riderDelivery, @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<RiderDeliverySmallDO> queryDeliverySmall(RiderDeliverySmallDO riderDeliverySmallDO);

    UserInfoDO queryUserInfoBySmallNo(String orderNo);

    void insertBig(RiderDelivery riderDelivery);

    void insertSmall(RiderDeliverySmallDO riderDeliverySmallDO);

    void insertSmalls(@Param("emps") List<RiderDeliverySmallDO> list);

    void upBigBy(RiderDelivery riderDelivery);

    void upBigStatus(@Param("handleNo") String handleNo, @Param("status") Integer status);

    void deleteSmallByHandleNo(RiderDeliverySmallDO riderDeliverySmallDO);
}
