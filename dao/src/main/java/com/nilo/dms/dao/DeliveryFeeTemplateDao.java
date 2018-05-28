package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.BizFeeConfigDO;
import com.nilo.dms.dao.dataobject.DeliveryFeeTemplateDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryFeeTemplateDao extends BaseDao<Long, DeliveryFeeTemplateDO> {

    List<DeliveryFeeTemplateDO> findAll();

    List<DeliveryFeeTemplateDO> findAllBy(@Param("merchantId") Long merchantId, @Param("country") String country, @Param("orderType") String orderType, @Param("orderPlatform") String orderPlatform, @Param("goodsType") String goodsType, @Param("area") String area);

    DeliveryFeeTemplateDO queryByOther(@Param("merchantId") Long merchantId, @Param("country") String country, @Param("orderType") String orderType, @Param("orderPlatform") String orderPlatform, @Param("goodsType") String goodsType);

    DeliveryFeeTemplateDO queryBy(DeliveryFeeTemplateDO templateDO);

}
