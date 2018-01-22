package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum NatureTypeEnum implements EnumMessage {

    FOREIGN_COMPANY("f_c", "Foreign Enterprises"),
    PRIVATE_COMPANY("p_c", "Private Enterprises"),
    STATE_OWNED_COMPANY("soc", "State Owned Enterprises"),;

    private String code;
    private String desc;

    private static Map<String, NatureTypeEnum> map = new HashMap<String, NatureTypeEnum>(10);

    static {
        NatureTypeEnum[] enums = NatureTypeEnum.values();
        for (NatureTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("NatureTypeEnum", map);

    }

    private NatureTypeEnum(String code, String desc) {
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

    public static NatureTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
