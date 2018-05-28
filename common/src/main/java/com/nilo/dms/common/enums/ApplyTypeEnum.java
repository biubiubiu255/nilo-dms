package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum ApplyTypeEnum implements EnumMessage {
    DELIVERY_ORDER_NO("delivery_order_no", "delivery order no"),
    ABNORMAL_DELIVERY_ORDER_NO("abnormal_order_no", "abnormal_order_no"),;
    private String code;
    private String desc;

    private static Map<String, ApplyTypeEnum> map = new HashMap<String, ApplyTypeEnum>(10);

    static {
        ApplyTypeEnum[] enums = ApplyTypeEnum.values();
        for (ApplyTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("ApplyTypeEnum", map);

    }

    private ApplyTypeEnum(String code, String desc) {
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

    public static ApplyTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
