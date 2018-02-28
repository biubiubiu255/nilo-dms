package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum DeliveryOrderStatusEnum implements EnumMessage{

    CREATE(0, "Create"),
    DISPATCH_TO_DMS(1, "Delivered to DMS"),
    ALLOCATED(2, "Allocated"),
    GO_PICK_UP(4, "Goto Pickup"),
    PICK_UP(5, "Pickup"),
    PICK_UP_FAILED(6, "Pickup failed"),
    ARRIVED(20, "Arrived"),
    LOADING(25, "Loading"),
    DELIVERY(30, "Delivery"),
    SEND(31, "Send"),
    DETAIN(35, "Detain"),
    PROBLEM(40, "Problem"),

    RECEIVED(50, "Received"), //change this value

    CANCELED(60, "Canceled"),;

    private Integer code;
    private String desc;

    private static Map<Integer, DeliveryOrderStatusEnum> map = new HashMap<Integer, DeliveryOrderStatusEnum>(20);

    static {
        DeliveryOrderStatusEnum[] enums = DeliveryOrderStatusEnum.values();
        for (DeliveryOrderStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DeliveryOrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static DeliveryOrderStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
