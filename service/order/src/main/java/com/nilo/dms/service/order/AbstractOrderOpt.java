package com.nilo.dms.service.order;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dao.DeliveryOrderDao;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.service.system.model.OrderHandleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017/11/3.
 */
public abstract class AbstractOrderOpt {

    private static Logger logger = LoggerFactory.getLogger(AbstractOrderOpt.class);

    @Autowired
    DeliveryOrderOptDao deliveryOrderOptDao;

    @Autowired
    DeliveryOrderDao deliveryOrderDao;

    protected void checkOtpParam(OrderOptRequest optRequest) {
        AssertUtil.isNotNull(optRequest, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(optRequest.getOptType(), BizErrorCode.OPT_TYP_EMPTY);
        AssertUtil.isNotBlank(optRequest.getMerchantId(), BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotNull(optRequest.getOrderNo(), BizErrorCode.ORDER_NO_EMPTY);
        AssertUtil.isNotBlank(optRequest.getOptBy(), BizErrorCode.OPT_USER_EMPTY);
    }

    protected void checkOptType(OrderOptRequest optRequest) {

        OrderHandleConfig config = SystemConfig.getOrderHandleConfig(optRequest.getMerchantId(), optRequest.getOptType().getCode());
        if (config == null) {
            throw new DMSException(BizErrorCode.HANDLE_TYPE_NOT_CONFIG, optRequest.getOptType().getCode());
        }
        List<Integer> allowStatus = config.getAllowStatus();
        List<Integer> notAllowStatus = config.getNotAllowStatus();

        for (String orderNo : optRequest.getOrderNo()) {

            DeliveryOrderDO orderDO = deliveryOrderDao.queryByOrderNo(Long.parseLong(optRequest.getMerchantId()), orderNo);
            if (orderDO == null) {
                throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, orderNo);
            }
            //判断订单此时状态是否在限制范围内
            boolean allow = false;
            if (allowStatus != null) {
                for (Integer s : allowStatus) {
                    if (s == orderDO.getStatus()) {
                        allow = true;
                        break;
                    }
                }
            }

            if (notAllowStatus != null) {
                for (Integer s : notAllowStatus) {
                    if (s == orderDO.getStatus()) {
                        allow = false;
                        break;
                    }
                }
            }
            if (!allow) {
                throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED, orderNo);
            }
        }
    }

    protected void addOptLog(OrderOptRequest optRequest, DeliveryOrderStatusEnum before, DeliveryOrderStatusEnum after) {
        try {
            for (String orderNo : optRequest.getOrderNo()) {
                DeliveryOrderOptDO optDO = new DeliveryOrderOptDO();
                optDO.setOrderNo(orderNo);
                optDO.setMerchantId(Long.parseLong(optRequest.getMerchantId()));
                optDO.setOpt(optRequest.getOptType().getCode());
                optDO.setOptBy(optRequest.getOptBy());
                optDO.setFromStatus(before.getCode());
                if (after != null) {
                    optDO.setToStatus(after.getCode());
                } else {
                    optDO.setToStatus(before.getCode());
                }
                optDO.setRemark(optRequest.getRemark());
                deliveryOrderOptDao.insert(optDO);
            }
        } catch (Exception e) {
            logger.error("Add Opt Log Failed. optRequest:{}", optRequest, e);
        }
    }

}
