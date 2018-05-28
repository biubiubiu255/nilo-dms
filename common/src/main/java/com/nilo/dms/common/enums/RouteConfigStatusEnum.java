package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum RouteConfigStatusEnum implements EnumMessage{

    NORMAL(1, "Normal"),
    DELETE(2, "Delete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, RouteConfigStatusEnum> map = new HashMap<Integer, RouteConfigStatusEnum>(20);

    static {
        RouteConfigStatusEnum[] enums = RouteConfigStatusEnum.values();
        for (RouteConfigStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private RouteConfigStatusEnum(Integer code, String desc) {
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

    public static RouteConfigStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
