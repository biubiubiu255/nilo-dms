package com.nilo.dms.service.order.fee;

/**
 * Created by admin on 2017/12/14.
 */
public class BasicFee implements IDeliveryFee {

    private Double basicFee;

    public BasicFee(Double basicFee) {
        this.basicFee = basicFee;
    }

    public Double cost() {
        return basicFee;
    }
}
