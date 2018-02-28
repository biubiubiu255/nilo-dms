package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;


public enum DeliveryOrderPaidTypeEnum implements EnumMessage{

	CASH(0, "Cash"),
	ONLINE(1, "Online"),
	ONLINE_PROBLEM(2, "Online Problem"),
   ;

    private Integer code;
    private String desc;

    private static Map<Integer, DeliveryOrderPaidTypeEnum> map = new HashMap<Integer, DeliveryOrderPaidTypeEnum>(20);

    static {
        DeliveryOrderPaidTypeEnum[] enums = DeliveryOrderPaidTypeEnum.values();
        for (DeliveryOrderPaidTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DeliveryOrderPaidTypeEnum(Integer code, String desc) {
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

    public static DeliveryOrderPaidTypeEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
