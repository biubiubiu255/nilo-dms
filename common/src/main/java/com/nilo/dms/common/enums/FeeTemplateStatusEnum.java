package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum FeeTemplateStatusEnum implements EnumMessage{

    NORMAL(1, "Normal"),
    DELETE(2, "Delete"),
    HISTORY(3, "History"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, FeeTemplateStatusEnum> map = new HashMap<Integer, FeeTemplateStatusEnum>(20);

    static {
        FeeTemplateStatusEnum[] enums = FeeTemplateStatusEnum.values();
        for (FeeTemplateStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private FeeTemplateStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static FeeTemplateStatusEnum getEnum(Integer code) {
        return map.get(code);
    }
    public Map getMap() {
        return map;
    }

}
