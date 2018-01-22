package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SignByTypeEnum implements EnumMessage{
    SELF("1", "Self"),
    OTHER("2", "Other"),;
    private String code;
    private String desc;

    private static Map<String, SignByTypeEnum> map = new HashMap<String, SignByTypeEnum>(10);

    static {
        SignByTypeEnum[] enums = SignByTypeEnum.values();
        for (SignByTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("SignByTypeEnum", map);
    }

    private SignByTypeEnum(String code, String desc) {
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

    public static SignByTypeEnum getEnum(String code) {
        return map.get(code);
    }
}
