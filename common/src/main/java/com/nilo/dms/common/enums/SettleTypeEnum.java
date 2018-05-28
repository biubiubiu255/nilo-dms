package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SettleTypeEnum implements EnumMessage{
    XIANJIE("xianjie", "现结"),;
    private String code;
    private String desc;

    private static Map<String, SettleTypeEnum> map = new HashMap<String, SettleTypeEnum>(10);

    static {
        SettleTypeEnum[] enums = SettleTypeEnum.values();
        for (SettleTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("SettleTypeEnum",map);
    }

    private SettleTypeEnum(String code, String desc) {
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

    public static SettleTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
