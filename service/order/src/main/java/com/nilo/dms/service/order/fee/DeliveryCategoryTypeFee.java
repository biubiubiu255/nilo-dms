package com.nilo.dms.service.order.fee;

import com.nilo.dms.common.enums.DeliveryCategoryTypeEnum;
import com.nilo.dms.common.enums.ServiceTypeEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.dataobject.DeliveryFeeFactorDO;

import java.util.List;

/**
 * Created by admin on 2017/12/14.
 */
public class DeliveryCategoryTypeFee implements IDeliveryFee {

    private IDeliveryFee deliveryFee;

    private DeliveryCategoryTypeEnum type;

    private List<DeliveryFeeFactorDO> factorDOList;

    public DeliveryCategoryTypeFee(IDeliveryFee fee, DeliveryCategoryTypeEnum type, List<DeliveryFeeFactorDO> factorDOList) {
        this.deliveryFee = fee;
        this.type = type;
        this.factorDOList = factorDOList;
    }

    private DeliveryCategoryTypeFee() {
    }

    public Double cost() {

        DeliveryFeeFactorDO factorDO = null;
        for (DeliveryFeeFactorDO f : factorDOList) {
            if (StringUtil.equals(f.getFactorType(), "category_type")) {
                factorDO = f;
                break;
            }
        }

        if (factorDO != null && StringUtil.equals(type.getCode(), factorDO.getFactorCode())) {

            double fee = deliveryFee.cost();

            switch (factorDO.getOperator()) {
                case "*": {
                    fee = fee * factorDO.getFee();
                    break;
                }
                case "+": {
                    fee = fee + factorDO.getFee();
                    break;
                }
                default:
                    break;
            }

            return fee;

        } else {
            return deliveryFee.cost();
        }
    }

}
