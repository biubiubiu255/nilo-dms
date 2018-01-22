package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SystemTypeEnum implements EnumMessage {
    ADMIN("HT", "HT"),
    COMPANY("CP", "CP"),;
    private String code;
    private String desc;

    private static Map<String, SystemTypeEnum> map = new HashMap<String, SystemTypeEnum>(10);

    static {
        SystemTypeEnum[] enums = SystemTypeEnum.values();
        for (SystemTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("SystemTypeEnum", map);

    }

    private SystemTypeEnum(String code, String desc) {
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

    public static SystemTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
