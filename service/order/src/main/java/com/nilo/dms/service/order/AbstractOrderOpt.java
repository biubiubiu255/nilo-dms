package com.nilo.dms.service.order;

import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dao.WaybillLogDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.system.OrderHandleConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static com.nilo.dms.common.Constant.IS_PACKAGE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/3.
 */
public abstract class AbstractOrderOpt {

    private static Logger logger = LoggerFactory.getLogger(AbstractOrderOpt.class);

    @Autowired
    WaybillLogDao waybillLogDao;

    @Autowired
    WaybillDao waybillDao;

    protected void checkOtpParam(OrderOptRequest optRequest) {
        AssertUtil.isNotNull(optRequest, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(optRequest.getOptType(), BizErrorCode.OPT_TYP_EMPTY);
        AssertUtil.isNotNull(optRequest.getOrderNo(), BizErrorCode.ORDER_NO_EMPTY);
    }

    protected void checkOptType(OrderOptRequest optRequest) {

        String merchantId = SessionLocal.getPrincipal().getMerchantId();

        OrderHandleConfig config = SystemConfig.getOrderHandleConfig(merchantId, optRequest.getOptType().getCode());
        if (config == null) {
            throw new DMSException(BizErrorCode.HANDLE_TYPE_NOT_CONFIG, optRequest.getOptType().getCode());
        }
        List<Integer> allowStatus = config.getAllowStatus();
        List<Integer> notAllowStatus = config.getNotAllowStatus();

        for (String orderNo : optRequest.getOrderNo()) {

            WaybillDO orderDO = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
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
            } else {
                allow = true;
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

    protected void unPackage(OrderOptRequest optRequest) {

        Long merchantId = Long.valueOf(SessionLocal.getPrincipal().getMerchantId());
        List<String> orders = new ArrayList<String>();
        for (String orderNo : optRequest.getOrderNo()) {
            WaybillDO orderDO = waybillDao.queryByOrderNo(merchantId, orderNo);
            if(!orderDO.getIsPackage().equals(IS_PACKAGE)){
                return ;
            }

            List<WaybillDO> childrenWaybill = waybillDao.queryByPackageNo(merchantId, orderNo);
            List<String> childrenOrders = new ArrayList<String>();
            for (WaybillDO e : childrenWaybill){
                childrenOrders.add(e.getOrderNo());
            }
            orders.addAll(childrenOrders);

        }
        optRequest.getOrderNo().addAll(orders);
    }



}
