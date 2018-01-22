package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum DeliveryCategoryTypeEnum implements EnumMessage{
    FILE("file", "File"),
    PACKAGE("package", "Package"),;
    private String code;
    private String desc;

    private static Map<String, DeliveryCategoryTypeEnum> map = new HashMap<String, DeliveryCategoryTypeEnum>(10);

    static {
        DeliveryCategoryTypeEnum[] enums = DeliveryCategoryTypeEnum.values();
        for (DeliveryCategoryTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("DeliveryCategoryTypeEnum",map);

    }

    private DeliveryCategoryTypeEnum(String code, String desc) {
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

    public static DeliveryCategoryTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
