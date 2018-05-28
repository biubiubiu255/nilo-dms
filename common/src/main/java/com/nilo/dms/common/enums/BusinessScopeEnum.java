package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum BusinessScopeEnum implements EnumMessage{
    NORMAL("1", "Normal Product"),
    FRESH("2", "Fresh Product"),
    DANGER("3", "Danger Product"),
    ;
    private String code;
    private String desc;

    private static Map<String, BusinessScopeEnum> map = new HashMap<String, BusinessScopeEnum>(10);

    static {
        BusinessScopeEnum[] enums = BusinessScopeEnum.values();
        for (BusinessScopeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("BusinessScopeEnum", map);

    }

    private BusinessScopeEnum(String code, String desc) {
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

    public static BusinessScopeEnum getEnum(String code) {
        return map.get(code);
    }

}
