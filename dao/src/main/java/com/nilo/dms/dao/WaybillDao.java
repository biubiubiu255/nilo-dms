package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.WaybillDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface WaybillDao extends BaseDao<Long,WaybillDO> {

    List<WaybillDO> queryDeliveryOrderListBy(Map map);

    Long queryCountBy(Map map);

    WaybillDO queryByReferenceNo(@Param("merchantId")Long merchantId, @Param("referenceNo") String referenceNo);

    WaybillDO queryByOrderNo(@Param("merchantId")Long merchantId, @Param("orderNo")String orderNo);

    List<WaybillDO> queryByOrderNos(@Param("merchantId")Long merchantId, @Param("orderNos")List<String> orderNos);

    List<WaybillDO> queryByPackageNo(@Param("merchantId")Long merchantId, @Param("packageNo") String packageNo);

    List<WaybillDO> queryAllNotCancellation(@Param("merchantId")Long merchantId, @Param("userId") String userId, @Param("offset")Integer offset, @Param("limit")Integer limit);

    Long queryAllNotCancellationCount(@Param("merchantId")Long merchantId,@Param("userId") String userId);

    List<WaybillDO> selectByOrderNos(@Param("orderNos")List<String> orderNos);
    
    List<String> findCreatePackageByNetWork(@Param("nextNetwork") String nextNetwork);

    void finishPackage(@Param("packageNo") String packageNo,@Param("weight") Double weight);
    
    List<WaybillDO> queryByUpdateTime(@Param("updatedTime") Long updatedTime);
}
