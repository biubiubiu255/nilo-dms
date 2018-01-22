package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum DeliveryFeeBizTypeEnum implements EnumMessage{
    NORMAL("normal", "normal"),
    REJECT("reject", "reject"),;
    private String code;
    private String desc;

    private static Map<String, DeliveryFeeBizTypeEnum> map = new HashMap<String, DeliveryFeeBizTypeEnum>(10);

    static {
        DeliveryFeeBizTypeEnum[] enums = DeliveryFeeBizTypeEnum.values();
        for (DeliveryFeeBizTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DeliveryFeeBizTypeEnum(String code, String desc) {
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

    public static DeliveryFeeBizTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
