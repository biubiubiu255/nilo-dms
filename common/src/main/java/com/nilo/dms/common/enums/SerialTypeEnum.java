package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SerialTypeEnum implements EnumMessage{
    STAFF_ID("staff_id", "STAFF_ID"),
    DELIVERY_ORDER_NO("delivery_order_no", "delivery order no"),
    ABNORMAL_DELIVERY_ORDER_NO("abnormal_order_no", "abnormal_order_no"),
    LOADING_NO("loading_no", "loading_no"),
    ;
    private String code;
    private String desc;

    private static Map<String, SerialTypeEnum> map = new HashMap<String, SerialTypeEnum>(10);

    static {
        SerialTypeEnum[] enums = SerialTypeEnum.values();
        for (SerialTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private SerialTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SerialTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
