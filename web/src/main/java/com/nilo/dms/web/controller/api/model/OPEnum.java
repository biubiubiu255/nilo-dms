package com.nilo.dms.web.controller.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum OPEnum {
    CREATE_DELIVERY_ORDER("create_delivery_order", "create_delivery_order"),
    ARRIVE_SCAN("arrive_scan", "arrive_scan"),
    SIGN("sign", "sign"),
    ABNORMAL("abnormal", "abnormal"),
    ;
    private String code;
    private String value;

    private static Map<String, OPEnum> map = new HashMap<String, OPEnum>(10);

    static {
        OPEnum[] enums = OPEnum.values();
        for (OPEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private OPEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public static OPEnum getEnum(String code) {
        return map.get(code);
    }
}
