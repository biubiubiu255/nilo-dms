package com.nilo.dms.web.controller.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum MethodEnum {
    CREATE_DELIVERY_ORDER("nos.waybill.create", "create_delivery_order"),
    ARRIVE_SCAN("arrive_scan", "arrive_scan"),
    SIGN("sign", "sign"),
    ABNORMAL("abnormal", "abnormal"),
    ;
    private String code;
    private String value;

    private static Map<String, MethodEnum> map = new HashMap<String, MethodEnum>(10);

    static {
        MethodEnum[] enums = MethodEnum.values();
        for (MethodEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private MethodEnum(String code, String value) {
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
    public static MethodEnum getEnum(String code) {
        return map.get(code);
    }
}
