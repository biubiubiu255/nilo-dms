package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.HandleSignDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleSignDao extends BaseDao<Long, HandleSignDO> {

    HandleSignDO queryByNo(@Param("merchantId") Long merchantId, @Param("orderNo") String  orderNo);

}
