package com.nilo.dms.service.order.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/10.
 */
@Data
public class DeliveryRoute {

    private String merchantId;

    private String orderNo;

    private String opt;

    private String optNetwork;

    private String networkDesc;

    private String optByName;

    private String optBy;

    private Long optTime;

    private String phone;

    private String remark;

    private Long createdTime;

}
