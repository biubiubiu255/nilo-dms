package com.nilo.dms.common.enums;

import com.nilo.dms.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alvin on 2018/07/11.
 */
public enum DeliveryOrderStatusEnum implements EnumMessage{


    CREATE(0, "Create"),
    ALLOCATED(10, "Allocated"),
    ARRIVED(20, "Arrived"),

    DELIVERY(30, "Delivery"),
    PROBLEM(40, "Problem"),
    REFUSE(60, "Refuse"),
    SIGN(50, "Sign"),
    CANCEL(90, "Cancel"),
    ; //change this value

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
