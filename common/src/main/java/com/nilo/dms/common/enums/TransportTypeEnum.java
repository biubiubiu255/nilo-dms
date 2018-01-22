package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum TransportTypeEnum implements EnumMessage{
    MOTORCYCLE("motorcycle", "Motorcycle"),
    CAR("car", "Car"),;

    private String code;
    private String desc;

    private static Map<String, TransportTypeEnum> map = new HashMap<String, TransportTypeEnum>(10);

    static {
        TransportTypeEnum[] enums = TransportTypeEnum.values();
        for (TransportTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("TransportTypeEnum",map);
    }

    private TransportTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TransportTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
