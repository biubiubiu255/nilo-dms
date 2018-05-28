package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum LevelEnum implements EnumMessage{
    BRONZE("1", "Bronze"),
    SILVER("2", "Silver"),
    GOLDEN("3", "Golden"),
    ;
    private String code;
    private String desc;

    private static Map<String, LevelEnum> map = new HashMap<String, LevelEnum>(10);

    static {
        LevelEnum[] enums = LevelEnum.values();
        for (LevelEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("LevelEnum", map);

    }

    private LevelEnum(String code, String desc) {
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

    public static LevelEnum getEnum(String code) {
        return map.get(code);
    }

}
