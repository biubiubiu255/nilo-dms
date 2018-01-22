package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum ServiceTypeEnum implements EnumMessage{
    ARRIVE_TODAY("1", "Arrive Today"),
    ARRIVE_TOMORROW("2", "Arrive Tomorrow"),
    NORMAL("3", "Normal"),
    ;
    private String code;
    private String desc;

    private static Map<String, ServiceTypeEnum> map = new HashMap<String, ServiceTypeEnum>(10);

    static {
        ServiceTypeEnum[] enums = ServiceTypeEnum.values();
        for (ServiceTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private ServiceTypeEnum(String code, String desc) {
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

    public static ServiceTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
