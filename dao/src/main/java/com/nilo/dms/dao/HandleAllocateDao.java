package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.HandleSignDO;
import com.nilo.dms.dto.handle.HandleAllocate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleAllocateDao extends BaseDao<Long, HandleAllocate> {

    long batchInsert(List<HandleAllocate> list);

    HandleAllocate queryByOrderNo(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo);
}
