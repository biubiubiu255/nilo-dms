package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.ThirdPushDo;
import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThirdPushDao extends BaseDao<Integer, ThirdPushDo>{

    List<ThirdPushDo> queryBy(ThirdPushDo thirdPushDo);

    int updateByOrderId(@Param("ThirdPushDo") ThirdPushDo thirdPushDo, @Param("orderId") String orderId );
}
