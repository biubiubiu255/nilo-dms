package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum FetchTypeEnum implements EnumMessage{
    DOOR_PICK_UP("1", "上门取件"),
    SEND_BY_SELF("2", "网点自寄"),;
    private String code;
    private String desc;

    private static Map<String, FetchTypeEnum> map = new HashMap<String, FetchTypeEnum>(10);

    static {
        FetchTypeEnum[] enums = FetchTypeEnum.values();
        for (FetchTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private FetchTypeEnum(String code, String desc) {
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

    public static FetchTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
