package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.DeliveryFeeDetailsStatusEnum;
import com.nilo.dms.dao.DeliveryFeeDetailsDao;
import com.nilo.dms.dao.DeliveryFeeFactorDao;
import com.nilo.dms.dao.DeliveryFeeTemplateDao;
import com.nilo.dms.dao.dataobject.DeliveryFeeDetailsDO;
import com.nilo.dms.dao.dataobject.DeliveryFeeFactorDO;
import com.nilo.dms.dao.dataobject.DeliveryFeeTemplateDO;
import com.nilo.dms.service.order.DeliveryFeeDetailsService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.order.fee.*;
import com.nilo.dms.service.order.model.DeliveryFeeDetails;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.model.BizFeeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/12/13.
 */
@Service
public class DeliveryFeeDetailsServiceImpl implements DeliveryFeeDetailsService {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private DeliveryFeeDetailsDao deliveryFeeDetailsDao;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private DeliveryFeeTemplateDao deliveryFeeTemplateDao;

    @Autowired
    private DeliveryFeeFactorDao deliveryFeeFactorDao;

    @Override
    public void buildDeliveryFee(String merchantId, String orderNo, String bizType) {

        //get bizFee
        BizFeeConfig bizFeeConfig = JSON.parseObject(RedisUtil.hget(Constant.BIZ_FEE_CONF + merchantId, bizType), BizFeeConfig.class);
        if (bizFeeConfig == null) {
            return;
        }
        Double bizFeeTimes = bizFeeConfig.getFee();

        //get order details
        Waybill order = waybillService.queryByOrderNo(merchantId, orderNo);

        //查找计费模板
        DeliveryFeeTemplateDO param = new DeliveryFeeTemplateDO();
        param.setMerchantId(Long.parseLong(order.getMerchantId()));
        param.setCountry(order.getCountry());
        param.setOrigin(order.getSenderInfo().getSenderCity());
        param.setDestination(order.getReceiverInfo().getReceiverCity());

        DeliveryFeeTemplateDO templateDO = deliveryFeeTemplateDao.queryBy(param);
        if (templateDO == null) {
            log.error("DeliveryFeeTemplate is not exist.");
            return;
        }

        //计费参数
        List<DeliveryFeeFactorDO> factorDOList = deliveryFeeFactorDao.queryBy(templateDO.getId());
        if (factorDOList == null || factorDOList.size() == 0) {
            log.error("DeliveryFeeFactor is null.");
        }

        //计算费用
        IDeliveryFee deliveryFee = new BasicFee(templateDO.getBasicFee());
        deliveryFee = new FirstWeightFee(deliveryFee,templateDO.getFirstFee());
        deliveryFee = new SecondWeightFeeI(deliveryFee, order.getWeight(), templateDO.getFirstRegion(), templateDO.getSecondRegion(), templateDO.getSecondFee());
        deliveryFee = new GoodsTypeFee(deliveryFee, order.getGoodsType(), factorDOList);

        DeliveryFeeDetailsDO detailsDO = new DeliveryFeeDetailsDO();
        detailsDO.setOrderNo(orderNo);
        detailsDO.setMerchantId(Long.parseLong(merchantId));
        detailsDO.setStatus(DeliveryFeeDetailsStatusEnum.CREATE.getCode());
        detailsDO.setBizType(bizType);
        detailsDO.setFee(deliveryFee.cost() * bizFeeTimes);

        deliveryFeeDetailsDao.insert(detailsDO);
    }

    @Override
    public void update(DeliveryFeeDetails network) {

        deliveryFeeDetailsDao.update(convert(network));

    }

    @Override
    public List<DeliveryFeeDetails> queryBy(String merchantId, String optType, Long beginTime, Long endTime, Pagination pagination) {
        return null;
    }

    @Override
    public DeliveryFeeDetails queryByCode(String merchantId, String code) {
        return null;
    }

    private DeliveryFeeDetailsDO convert(DeliveryFeeDetails deliveryFeeDetails) {

        DeliveryFeeDetailsDO deliveryFeeDetailsDO = new DeliveryFeeDetailsDO();
        deliveryFeeDetailsDO.setMerchantId(Long.parseLong(deliveryFeeDetails.getMerchantId()));
        deliveryFeeDetailsDO.setBizType(deliveryFeeDetails.getBizType());
        deliveryFeeDetailsDO.setOrderNo(deliveryFeeDetails.getOrderNo());
        deliveryFeeDetailsDO.setRemark(deliveryFeeDetails.getRemark());
        if (deliveryFeeDetails.getStatus() != null) {
            deliveryFeeDetailsDO.setStatus(deliveryFeeDetails.getStatus().getCode());
        }
        return deliveryFeeDetailsDO;

    }

}
