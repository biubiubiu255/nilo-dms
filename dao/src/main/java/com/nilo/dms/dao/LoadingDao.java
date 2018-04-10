package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.LoadingDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface LoadingDao extends BaseDao<Long,LoadingDO> {

    List<LoadingDO> queryListBy(@Param("merchantId") Long merchantId,@Param("loadingNo") String loadingNo,@Param("status") Integer status,@Param("offset") int offset, @Param("limit") int limit);

    LoadingDO queryByLoadingNo(@Param("merchantId") Long merchantId, @Param("loadingNo") String loadingNo);
}
