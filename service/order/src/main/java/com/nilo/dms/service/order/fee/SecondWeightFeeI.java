package com.nilo.dms.service.order.fee;

/**
 * Created by admin on 2017/12/14.
 */
public class SecondWeightFeeI implements IDeliveryFee {

    private IDeliveryFee deliveryFee;

    private double firstWeight;

    private double weight;

    private double secondWeight;

    private double secondFee;

    public SecondWeightFeeI(IDeliveryFee factor, double weight, double firstWeight, double secondWeight, double secondFee) {
        this.deliveryFee = factor;
        this.secondFee = secondFee;
        this.firstWeight = firstWeight;
        this.weight = weight;
        this.secondWeight = secondWeight;
    }

    private SecondWeightFeeI() {
    }

    public Double cost() {

        if (weight > firstWeight) {
            //向上取整
            int second = (int) Math.ceil((weight - firstWeight) / secondWeight);

            return second * secondFee + deliveryFee.cost();
        }
        return deliveryFee.cost();
    }

}
