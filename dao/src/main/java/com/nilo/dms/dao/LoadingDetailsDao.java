package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.LoadingDO;
import com.nilo.dms.dao.dataobject.LoadingDetailsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface LoadingDetailsDao extends BaseDao<Long, LoadingDetailsDO> {

    List<LoadingDetailsDO> queryByLoadingNo(String loadingNo);

    List<LoadingDetailsDO> queryByLoadingNos(@Param(value = "loadingNos") List<String> loadingNos);

    Integer deleteBy(@Param(value = "loadingNo") String loadingNo, @Param(value = "orderNo") String orderNo);

    LoadingDetailsDO queryByOrderNo(@Param(value = "loadingNo") String loadingNo, @Param(value = "orderNo") String orderNo);

}
