package com.nilo.dms.service.order.fee;

import com.nilo.dms.common.enums.DeliveryCategoryTypeEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.dataobject.DeliveryFeeFactorDO;

import java.util.List;

/**
 * Created by admin on 2017/12/14.
 */
public class GoodsTypeFee implements IDeliveryFee {

    private IDeliveryFee deliveryFee;

    private String goodsType;

    private List<DeliveryFeeFactorDO> factorDOList;

    public GoodsTypeFee(IDeliveryFee fee, String goodsType, List<DeliveryFeeFactorDO> factorDOList) {
        this.deliveryFee = fee;
        this.goodsType = goodsType;
        this.factorDOList = factorDOList;
    }

    private GoodsTypeFee() {
    }

    public Double cost() {

        DeliveryFeeFactorDO factorDO = null;
        for (DeliveryFeeFactorDO f : factorDOList) {
            if (StringUtil.equals(f.getFactorType(), "goods_type")) {
                factorDO = f;
                break;
            }
        }

        if (factorDO != null && StringUtil.equals(goodsType, factorDO.getFactorCode())) {

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
