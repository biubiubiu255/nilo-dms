package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.ImageDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageDao extends BaseDao<Long, ImageDO> {

    List<ImageDO> findBy(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo, @Param(value = "imageType") String imageType);

}
