package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum LoadingTypeEnum implements EnumMessage {
    DELIVERY("delivery", "delivery order no"),
    SEND("send", "send"),;
    private String code;
    private String desc;

    private static Map<String, LoadingTypeEnum> map = new HashMap<String, LoadingTypeEnum>(10);

    static {
        LoadingTypeEnum[] enums = LoadingTypeEnum.values();
        for (LoadingTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("LoadingTypeEnum", map);

    }

    private LoadingTypeEnum(String code, String desc) {
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

    public static LoadingTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
