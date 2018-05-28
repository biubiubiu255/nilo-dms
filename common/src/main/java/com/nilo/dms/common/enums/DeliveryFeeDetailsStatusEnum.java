package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum DeliveryFeeDetailsStatusEnum implements EnumMessage {

    CREATE(1, "Create"),
    CANCELED(2, "Canceled"),
    COMPLETE(3, "Complete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, DeliveryFeeDetailsStatusEnum> map = new HashMap<Integer, DeliveryFeeDetailsStatusEnum>(20);

    static {
        DeliveryFeeDetailsStatusEnum[] enums = DeliveryFeeDetailsStatusEnum.values();
        for (DeliveryFeeDetailsStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DeliveryFeeDetailsStatusEnum(Integer code, String desc) {
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

    public static DeliveryFeeDetailsStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
