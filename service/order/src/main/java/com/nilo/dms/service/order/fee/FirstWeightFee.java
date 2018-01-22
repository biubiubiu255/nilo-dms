package com.nilo.dms.service.order.fee;

/**
 * Created by admin on 2017/12/14.
 */
public class FirstWeightFee implements IDeliveryFee {

    private IDeliveryFee deliveryFee;

    private Double firstFee;

    public FirstWeightFee(IDeliveryFee deliveryFee,Double firstFee) {
        this.firstFee = firstFee;
        this.deliveryFee = deliveryFee;
    }

    public Double cost() {
        return firstFee+deliveryFee.cost();
    }
}
