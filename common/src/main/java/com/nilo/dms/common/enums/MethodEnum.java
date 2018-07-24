package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/2/6.
 */
public enum MethodEnum {
    CREATE_WAYBILL("nos.waybill.create", "nos.waybill.create"),
    CANCEL_WAYBILL("nos.waybill.cancel", "nos.waybill.cancel"),
    WAYBILL_TRACE("nos.waybill.trace","waybill.trace"),
    STATUS_UPDATE("nos.waybill.status","waybill.status"),
    UPDATE_WEIGHT("nos.waybill.weight", "nos.waybill.weight"),
    ARRIVE_SCAN("nos.waybill.arrive", "Arrive Scan"),
    SIGN("sign", "sign"),
    SUB_WAYBILL("nos.waybill.sub", "Sub Waybill"),
    THIRD_WAYBILL("nos.waybill.third", "Third Order Push"),
    THIRD_STATUS("nos.third.status", "Update third status"),
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
